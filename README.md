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
