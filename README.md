# VariaMosComponentManagement

VariaMosComponentManagement is a VariaMos external plugin, used to provide support to software product lines developed under the FragOP approach.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `variamos.componentmanagement.Main` class from your IDE (In Eclipse use: Run as -> Java Application).

## Running the application with Docker

Alternative, you can run this application with Docker, by following the next steps:

1. Install Docker (info [here](https://docs.docker.com/get-docker/)).
2. Run the next command:
`sudo docker run -d -p 8081:8081 --name variamos-cm danielgara/variamoscomponentmanagement`
3. Open the application in the port 8081.

## Functionalities

Used with VariaMos, this repo provides the next functionalities.

1. **Test Component Management Backend:** verify that the backend application is running properly.
2. **Upload Component Pool (.zip):** allows to upload the reusable domain components which were developed under the FragOP approach.
3. **Show File Code:** if the user clicks a cell that represents a fragment, file or custom file, then, this option allows to see the content of those files.
4. **Execute Derivation:** executes a product derivation based on the selected concrete features (in the FeatureModel).
5. **Customize Derivation:** allows to customize a derivation.
6. **Verify Derivation:** allows to verify a derivation (code syntax).
7. **Obtain product (.zip):** generates a zip file that contains the derived and customized product code.

More info [here](https://github.com/danielgara/VariaMos/wiki/External-Plugins-%E2%80%90-Fragment%E2%80%90oriented-programming-(FragOP)).
