# Red Hat JBoss EAP on Azure Red Hat OpenShift Hello World

This is a simple application that demonstrates a basic deployment of an application Azure Red Hat OpenShift.

![Screenshot](src/main/webapp/assets/img/page.png)

## Steps to deploy EAP app to Azure Red Hat OpenShift

From the Developer UI, create a new OpenShift project e.g "eap". 

Click on "Project" and then click on "create a Project".  

![Create project](src/main/webapp/assets/img/create-project.png)

Click on "Create" and then go to "Helm" and click on "Install a Helm Chart from the developer catalog"

![Helm install](src/main/webapp/assets/img/helm.png)

Enter "eap74" in the filter field.

![EAP Helm Chart](src/main/webapp/assets/img/helm-eap74.png)

Select "Eap74" and click on "Install Helm Chart"

In the "YAML view", paste the following yaml:

```
image:
  tag: latest
build:
  enabled: true
  mode: s2i
  uri: 'https://github.com/jamesfalkner/helloworld-eap.git'
  ref: main
  output:
    kind: ImageStreamTag
  env:
    - name: MAVEN_ARGS_APPEND
      value: '-Dcom.redhat.xpaas.repo.jbossorg'
  triggers: {}
  s2i:
    version: latest
    arch: amd64
    jdk: '11'
    amd64:
      jdk8:
        builderImage: registry.redhat.io/jboss-eap-7/eap74-openjdk8-openshift-rhel7
        runtimeImage: registry.redhat.io/jboss-eap-7/eap74-openjdk8-runtime-openshift-rhel7
      jdk11:
        builderImage: registry.redhat.io/jboss-eap-7/eap74-openjdk11-openshift-rhel8
        runtimeImage: registry.redhat.io/jboss-eap-7/eap74-openjdk11-runtime-openshift-rhel8
deploy:
  enabled: false
```

Click on "Install" to install the Helm chart.

Goto "Builds", you should see the following.

![EAP Builds](src/main/webapp/assets/img/builds.png)

These builds will take a few minutes to complete. Once they are done, we're going to deploy our JBoss EAP application using the EAP Operator.

Switch back to the Administrator UI, and goto "Operators" and "Operator Hub".  Enter "JBoss EAP" in the filter field.

![Operator Hub](src/main/webapp/assets/img/operator-hub.png)

Select "JBoss EAP" and click on "Install"


![Install EAP operator](src/main/webapp/assets/img/install-eap-operator.png)

From the "Install Operator" page, click on "Install" to install the JBoss EAP operator.

Once the JBoss EAP operator is installed, switch back to the Developer UI.

From the Developer UI. Ensure you're using the same project you installed the Helm chart in to create the EAP builds.

Click on "+Add" and select "Operator Backed".

From the list of options, select "WildflyServer", and then click on "Create"

Paste the following into the "YAML View".

```
apiVersion: wildfly.org/v1alpha1
kind: WildFlyServer
metadata:
  name: sample-eap
spec:
  applicationImage: 'eap74:latest'
  replicas: 1

```

Click on "Create" to deploy the EAP application.

Once the JBoss EAP application is successfully deployed on Azure Red Hat OpenShift, you will see the following in the "Topology" view.

![Topology view](src/main/webapp/assets/img/topology.png)


Click on the "Open Url" icon ![url icon](src/main/webapp/assets/img/open-url.png) , to view the application UI

The application is now successfully deployed to Azure Red Hat OpenShift.
