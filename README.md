This project includes several sample Fedora datasets that can be used for testing and demonstration.

## Example use

1. `git clone https://github.com/fcrepo4-labs/fcrepo-sample-dataset.git`
2. `tar -xzvf fcrepo-sample-dataset/additional_datasets/lubm/LUBM_02.tar.gz`
3. `curl -X POST -d "/path/to/extracted/LUBM_02" "http://localhost:8080/rest/fcr:restore`

## Resources

* https://wiki.duraspace.org/display/FEDORA4x/RESTful+HTTP+API+-+Backup+and+Restore

## Credits

* http://swat.cse.lehigh.edu/projects/lubm/
