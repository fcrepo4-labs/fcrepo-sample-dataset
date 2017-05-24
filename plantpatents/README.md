# Plant Patents Sample Dataset

Source: United States Patent and Trademark Office, www.uspto.gov.

The metadata and files in this dataset derive from government-produced documents that are in the public domain in the United States. For more information, see: http://www.uspto.gov/terms-use-uspto-websites.

The color scans were made from paper copies on file at the University of Maryland Libraries, as part of a project to make available color images of plants under patent, images which are filed with the patent application, but which are only available in black-and-white versions from the USPTO.

The data are structured as follows: 

  1. Each patent is a container node located at the root of the repository;
  2. Each patent has two child nodes, called "internal" and "external":
    - the internal child node is a nonRDFsource with a PDF binary attached;
    - the external child node is of the MIME-type "message/external-body" and points to a second copy of the same scan on the website of the University of Maryland Libraries;
  3. All patents are indirectly contained by the /plantpatents collection container node;
  4. The metadata attached to the container nodes are meant to provide some basic searchability in a test repository to which the data are loaded. The metadata includes plant name, inventor, inventor's city and country (as reported in the patent application), application number, patent number, US Patent classification code, type of plant, and a link to the corresponding entry in the USPTO database.
  5. A file containing sha-1 checksums for all of the original binaries, and another containing the URIs of the patent node and internal file node for each patent are included in the tests directory.  
