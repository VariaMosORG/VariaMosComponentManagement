package variamos.componentmanagement.services;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@EnableAutoConfiguration
public class Deployment {

	public static String derived_folder = "component_derived/";
	public static String deployment_main_folder = "deployment/";

	@CrossOrigin
	@RequestMapping("/deployment/test")
	String home() {
		return "Ok";
	}

	@CrossOrigin
	@RequestMapping(value = "/deployment/uploadAppsToGitHub", method = RequestMethod.POST, produces = "text/plain")
	@ResponseBody
	String uploadAppsToGitHub(@RequestBody String data_collected) {
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(data_collected).getAsJsonObject();
		JsonElement apps = rootObj.get("apps");
		String branchName = apps.getAsString();

		JsonElement p_derived = rootObj.get("p_derived");
		derived_folder = p_derived.getAsString();
		
		JsonElement p_repo = rootObj.get("repo");
		String repo = p_repo.getAsString();
		
		JsonElement p_token = rootObj.get("token");
		String token = p_token.getAsString();

		try {
			File appDeploymentDirectory = new File(deployment_main_folder + "/" + branchName);
			if (appDeploymentDirectory.exists()) {
				FileUtils.forceDelete(appDeploymentDirectory);
			}

			Git git = Git.cloneRepository().setURI("https://github.com/"+repo+".git")
					.setDirectory(appDeploymentDirectory).setCloneAllBranches(true).call();

			try {
				boolean existingBranch = false;
				List<Ref> call = git.branchList().setListMode(ListMode.ALL).call();
				for (Ref ref : call) {
					String currentBranch = ref.getName().substring(ref.getName().lastIndexOf('/') + 1);
					if (currentBranch.equals(branchName)) {
						existingBranch = true;
					}
				}

				if (existingBranch) {
					git.checkout().setName(branchName).setCreateBranch(true)
						.setStartPoint("origin/" + branchName).call();
				} else {
					git.branchCreate().setForce(true).setName(branchName).call();
					git.checkout().setName(branchName).call();
				}

				File appDeploymentDirectoryUpdated = new File(deployment_main_folder + "/" + branchName);

				for (File file : appDeploymentDirectoryUpdated.listFiles()) {
					if (file.getName().equals(".git")) {
						// Do nothing
					} else {
						file.delete();
					}
				}

				File appDirectory = new File(derived_folder + "/" + branchName);
				FileUtils.copyDirectory(appDirectory, appDeploymentDirectory);

				git.add().addFilepattern(".").call(); // stage new files
				git.add().setUpdate(true).addFilepattern(".").call(); // stage removed files
				git.commit().setMessage("Commit with new derivation").call();

				RemoteAddCommand remoteAddCommand = git.remoteAdd();
				remoteAddCommand.setName("origin");
				remoteAddCommand.setUri(new URIish("git@github.com:"+repo+".git"));
				remoteAddCommand.call();

				CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
						token, "");

				git.push().setRemote("origin").setCredentialsProvider(credentialsProvider).call();

				git.getRepository().close();
				git.close();
			} catch (Exception e) {
				git.getRepository().close();
				git.close();
				if(e.getMessage().contains("hung up unexpectedly")) {
					return "Success";
				}else {
					return "error" + e.getMessage();
				}
			}
		} catch (Exception e) {
			return "error" + e.getMessage();
		}

		return "Success";
	}
}
