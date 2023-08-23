
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.cdancy/bitbucket-rest/badge.png)](https://maven-badges.herokuapp.com/maven-central/io.github.cdancy/bitbucket-rest)
[![Stack Overflow](https://img.shields.io/badge/stack%20overflow-bitbucket&#8211;rest-4183C4.svg)](https://stackoverflow.com/questions/tagged/bitbucket+rest)

# bitbucket-client-java
![alt tag](https://wac-cdn.atlassian.com/dam/jcr:e2a6f06f-b3d5-4002-aed3-73539c56a2eb/bitbucket_rgb_blue.png?cdnVersion=cm)

java client, based on spring http interface, to interact with Bitbucket's REST API.

## On Spring 6.X, apis and endpoints
Being built on top of `Spring-webflux 6.X for use http interface client https://www.baeldung.com/spring-6-http-interface` means things are broken up into [Apis](https://github.com/Hillelmed/bitbucket-client-java/tree/master/src/main/java/com/cdancy/bitbucket/rest/features).
`Apis` are just Interfaces that are analagous to a resource provided by the server-side program (e.g. /api/branches, /api/pullrequest, /api/commits, etc..).
The methods within these Interfaces are analagous to an endpoint provided by these resources (e.g. GET /api/branches/my-branch, GET /api/pullrequest/123, DELETE /api/commits/456, etc..).
The user only needs to be concerned with which `Api` they need and then calling its various methods. These methods, much like any java library, return domain objects
(e.g. POJO's) modeled after the json returned by `bitbucket`.

Interacting with the remote service becomes transparent and allows developers to focus on getting
things done rather than the internals of the API itself, or how to build a client, or how to parse the json.

## On new features

New Api's or endpoints are generally added as needed and/or requested. If there is something you want
to see just open an ISSUE and ask or send in a PullRequest. However, putting together a PullRequest
for a new feature is generally the faster route to go as it's much easier to review a PullRequest
than to create one ourselves. There is no problem doing so of course but if you need something done
now than a PullRequest is your best bet otherwise you may have to patiently wait for one of our
contributors to take up the work.

* The next milestone is support reactive Mono<T> and Flux<T> objects
## Latest Release

Can be sourced from maven like so:

    <dependency>
      <groupId>io.github.hmedioni</groupId>
      <artifactId>bitbucket-client-java</artifactId>
      <version>X.Y.Z</version>
      <classifier>sources|tests|javadoc|all</classifier> (Optional)
    </dependency>

We use semantic version for upload and upgrade versions

### Maven repository Configuration:

* Download a sample settings.xml file that has configured profile to pull from the correct maven repository using this link: [settings.xml](https://bintray.com/repo/downloadMavenRepoSettingsFile/downloadSettings?repoPath=%2Fbintray%2Fjcenter)

**OR**

* Add the following repository to the project pom.xml file under project tag:
```
    <repositories>
      <repository>
        <snapshots>
          <enabled>false</enabled>
        </snapshots>
        <id>jcenter</id>
        <name>bintray</name>
        <url>http://jcenter.bintray.com</url>
      </repository>
    </repositories>
```
## Documentation

javadocs can be found via [github pages here](https://github.com/Hillelmed/bitbucket-client-java/)

## Examples on how to build a _BitbucketClient_

When using `Basic` (e.g. username and password) authentication:

    String url = "http://127.0.0.1:7990";
    String user = "admin";
    String password = "password";
    BitbucketProperties bitbucketProperties = new BitbucketProperties(url, user, password);
    BitbucketClient client = BitbucketClient.create(bitbucketProperties);

    Version version = client.api().systemApi().version();


When using `Anonymous` authentication or sourcing from system/environment (as described below):

    String url = "http://127.0.0.1:7990";
    BitbucketProperties bitbucketProperties = new BitbucketProperties(url);
    BitbucketClient client = BitbucketClient.create(bitbucketProperties);

    Version version = client.api().systemApi().version();

## On Overrides

Because we are built on top of Http interface using webClient we can take advantage of overriding various internal _HTTP_ properties by
passing in a `BitbucketClient.create` object WebClient and custom `BitbucketClient create(BitbucketProperties bitbucketProperties, WebClient webClient)` or, and in following with the spirit of this library, configuring them
found [HERE](https://docs.spring.io/spring-framework/reference/web/webflux-webclient/client-builder.html).

When configuring through a `BitbucketClient` object you must pass in create the customer WebClient:

            //Default webClient when use regular client create
            WebClient webClient = WebClient.builder()
                    .uriBuilderFactory(factory)
                    .filter(bitbucketAuthenticationFilter)
                    .filter(scrubNullFromPathFilter)
                    .filter(BitbucketErrorHandler.handler())
                    .build();

When Passing through `BitbucketClient` :

            WebClient myWebClient = WebClient.builder()
                    .uriBuilderFactory(url)
                    .filter(myFilterErrorHandling)
                    .build();
            BitbucketProperties bitbucketProperties = new BitbucketProperties(url);
            BitbucketClient client = BitbucketClient.create(bitbucketProperties,myWebClient);


## Understanding Error objects

When something pops server-side `bitbucket` will hand us back a list of [Error](https://github.com/Hillelmed/bitbucket-client-java/blob/master/src/main/java/com/cdancy/bitbucket/rest/domain/common/Error.java) objects. we're throwing an exception at runtime we attach this List of `Error` objects
The throwing object is Bitbucket [BitbucketAppException.java](https://github.com/Hillelmed/bitbucket-client-java/src%2Fmain%2Fjava%2Fio%2Fgithub%2Fhmedioni%2Fbitbucket%2Fclient%2Fexception%2FBitbucketAppException.java)
to most [domain](https://github.com/Hillelmed/bitbucket-client-java/tree/master/domain/src%2Fmain%2Fjava%2Fio%2Fgithub%2Fhmedioni%2Fbitbucket%2Fclient%2Fdomain) objects. Thus, it is up to the user to check the handed back domain object to see if the attached List is empty, and if not, iterate over the `Error` objects to see if it's something
truly warranting an exception. List of `Error` objects itself will always be non-null but in most cases empty (unless something has failed).

An example on how one might proceed:

    try {
        PullRequest pullRequest = client.api().pullRequestApi().get("MY-PROJECT", "MY-REPO", 99999).getBody();
    } catch (BitbucketAppException e) {
        for(Error error : e.errors()) {
            if (error.message().matches(".*Pull request \\d+ does not exist in .*")) {
                throw new RuntimeException(error.message());
            }
        }
    }


## Examples

The [mock](https://github.com/Hillelmed/bitbucket-client-java/tree/master/src/test/java/com/cdancy/bitbucket/rest/features) and [live](https://github.com/Hillelmed/bitbucket-client-java/tree/master/src/test/java/com/cdancy/bitbucket/rest/features) tests provide many examples
that you can use in your own code. If there are any questions feel free to open an issue and ask.

## Components

- Spring 6 \- used as the backend for communicating with Bitbucket's REST API
- Jackson data-bind \- used to create value serialized types both to and from the bitbucket program
- Lombok \- Automate getter setter contracture program

## Testing

Running mock tests can be done like so:

    ./mvn test -Punit

Running integration tests can be done like so (requires Bitbucket instance):

    ./mvn test -Plive

Various [properties](https://github.com/Hillelmed/bitbucket-client-java/tree/master/pom.xml) exist for you to configure

# Additional Resources

* [Bitbucket docker setup](https://bitbucket.org/atlassian/docker-atlassian-bitbucket-server)
* [Bitbucket REST API](https://developer.atlassian.com/static/rest/bitbucket-server/latest/bitbucket-rest.html)
* [Bitbucket Auth API](https://developer.atlassian.com/bitbucket/server/docs/latest/how-tos/example-basic-authentication.html)
* [Http interface Spring 6](https://www.baeldung.com/spring-6-http-interface)