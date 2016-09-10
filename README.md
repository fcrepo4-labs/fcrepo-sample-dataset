This project includes several sample Fedora datasets that can be used for testing and demonstration.

## Top Level Directory

The main directory includes sample data and a tool to upload the dataset into a Fedora repository.

To import the sample dataset:

1. Download the fcrepo-sample-dataset tool: git clone git@github.com:futures/fcrepo-sample-dataset.git
2. Change to the fcrepo-sample-dataset directory: cd fcrepo-sample-dataset
3. Build: mvn clean install
4. Run: mvn -Dfcrepo.url=&lt;repo.url&gt; exec:java

The whole dataset will be loaded into the Fedora repository <repo.url>, or view the dataset from http://localhost:8080/rest by default. 

To load your own dataset, convert it to jcr/xml format and place the data files under src/main/resources/data directory, then run "mvn -Dfcrepo.url=&lt;repo.url&gt; exec:java" to load it to the repository.

## Additional Datasets

These datasets were produced using Fedora's fcr:backup feature and can be loaded into a clean repository with fcr:restore.  See the [instructions on backup and restore on the Fedora 4 wiki](https://wiki.duraspace.org/display/FEDORA4x/RESTful+HTTP+API+-+Backup+and+Restore).
