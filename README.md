# Publishing to OSSRH
## Prerequisites
The publishing scripts and tools are in `maven-publishing-rules.gradle`.
To make a module publishable just add the following lines to the module's build.gradle:

```groovy
group 'me.mekarthedev.tools'
version '0.1.0'
description 'Module description'

apply from: mavenPublishingRules
```

Required PGP signing properties:

```
signing.keyId
signing.secretKeyRingFile
signing.password
```

Required OSSRH authentication properties:

```
ossrh.username
ossrh.password
```

The properties that are not explicitly set will be prompted during publishing.

## Uploading

* Run `gradle :{module-name}:uploadArchives` where module-name is the name of the module to be published.
* Go to https://oss.sonatype.org/#stagingRepositories and find the created repository.
* Press Close and Release.
