# Embed for Vaadin

Embed for Vaadin is a library allowing you to quickly initialize Vaadin from your IDE. It provides an easy-to-use API to initialize an embed Tomcat container running a particular component or Vaadin application.

The API also allows you to open the default browser automatically and allocates an available HTTP port if none is provided. The API is extensible so that you can suit it you to your own needs.

Embed for Vaadin works best when used with a build tool such as Maven as it requires Tomcat to operate. Make sure to add this dependency in scope `test` or make it optional such as these libraries do not get transmitted in your project.

# Building from Source

Clone the git repository using the URL on the Github home page:

    $ git clone git@github.com:bsblabs/embed-for-vaadin.git
    $ cd vaadin-embed

## Command Line

Use Maven, then on the command line:

    $ mvn install

Make sure you have access to the central repository.

# Getting started

The most easy way to experience with Embed for Vaadin is to use the simple [Maven archetype](http://maven.apache.org/guides/introduction/introduction-to-archetypes.html). The following command line creates a simple project:

    mvn archetype:generate  \
        -DarchetypeGroupId=com.bsb.common.vaadin \
        -DarchetypeArtifactId=com.bsb.common.vaadin.embed-simple-archetype \
        -DarchetypeVersion=0.4-SNAPSHOT

There are two ways to experience with the project once it has been created:

1. Open the project in your IDE and run the `DemoApp` main class
2. Execute the following command in the project

    mvn package exec:exec

# Usage

To use this library in your own projects, add a dependency to it using you favorite build tool. For instance, with Apache Maven:

    <dependency>
        <groupId>com.bsb.common.vaadin</groupId>
        <artifactId>com.bsb.common.vaadin.embed</artifactId>
        <version>0.3</version>
    </dependency>

To visualize a component that you have built, simply pass it to the `EmbedVaadin` builder.

    EmbedVaadin.forComponent(new Button("Hello")).start();

And if you want to open the default browser automatically at the proper URL, you could just add an extra parameter:

    EmbedVaadin.forComponent(new Button("Hello")).openBrowser(true).start();

You can as easily embed a Layout or a Window. Note that this mode is purely for development purpose as you pass an initialized component to the application. Multi-sessions are therefore not supported. You could also add a development header that allows you to shutdown the server and close the browser tab.

    EmbedVaadin.forComponent(new Button("Hello")).openBrowser(true).withDevelopmentHeader(true).start();

It is also possible to specify the `Class` of an `Application`. In that case, the server starts closer to the full thing.

    EmbedVaadin.forApplication(MyVaadinApplication.class).start();

The url that will be used to open the browser can be customized using the builder. For instance, the following would enable the Vaadin debug window:

    EmbedVaadin.forComponent(new Button("Hello")).openBrowserAt("?debug").start();

The parameter of the `openBrowserAt` method can be of the following types:

- An absolute url, i.e. starting with `http://`
- A relative reference, i.e. not starting with `/`
- An absolute reference, i.e. starting with `/`

References work exactly the same way as the HTML `href`, see the Javadoc of `openBrowserAt` for more details.

# Configuration

`EmbedVaadinConfig` is base configuration object holding the properties that any server should fulfill. We also provide `EmbedComponentConfig`, an extension when embedding a component since it has a few extra options. You could decide to extend from any of these if you need to support extra options.

There are two ways to change the default values of the embed server:

 - Through the presence of a `vaadin-embed.properties` file at the root of the classpath overriding default values
 - By using the API

At this stage the following can be customized:

 - `server.port`: the HTTP port the server listens to (an available port is taken by default)
 - `context.path`: the context of the generated web application (by default, the *root* context)
 - `context.rootDir`: the root directory of the generated web application to serve static files such as CSS and images (not necessary, unless you want to use themes or static resources)
 - `server.await`: to block the thread that started the server (by default, *true*)
 - `vaadin.widgetSet`: the name of a custom _WidgetSet_ to use for the application
 - `vaadin.productionMode`: to enable or disable the production mode. Disabled by default so that debug features are available
 - `open.browser`: to open the browser automatically at the proper url once the server has started
 - `browser.customUrl`: to customize the url that will be used to open the browser

When embedding a _component_, these extra options are also available:

- `vaadin.theme`: the vaadin theme to apply to the generated application. Ignored if an application class is used (by default, *reindeer*)
- `development.header`: to add a development header to the generated application. Right now, the development header allows you to shutdown the server and close the tab

This showcase how the API can be used to customize some of these settings:

        EmbedVaadin.forComponent(new Label("Hello World!"))
            .withContextPath("/foo")
            .withHttpPort(7070)
            .withContextRootDirectory("src/main/webapp")
            .withTheme("default")
            .openBrowser(true)
            .start();

# Extending

Custom extension can be added very easily by extending from the `EmbedVaadinServerBuilder`. These extensions can specify a custom configuration object, extending from `EmbedVaadinConfig`.

# Release notes

## 0.4 (not released yet)

- #7: customization of the url to use when opening the browser
- #6: added simple Maven archetype

## 0.3

- #5: support of Vaadin production mode
- #4: the development header is no longer added by default. Added an option to enable it
- #3: context path is handled the same when it is specifies through the API or loaded from a properties file
- EmbedVaadinConfig now exposes the deploy URL of the application. This takes into account the actual port that was allocated once the server has started
- #2: wrong startup log when a custom HTTP port is specified

## 0.2

* Custom WidgetSet can be specified
* Clearer separation between an embed server deploying a single component and one deploying a full application
* Development header: ability to shutdown the server and close the tab from the user interface
* Improved browser support
* A custom implementation can now expose a custom properties configuration more easily

## 0.1

Initial release

