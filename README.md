# Vaadin embed

The Vaadin embed is a library allowing you to quickly initialize Vaadin from your IDE. It provides an easy-to-use API to initialize an embed Tomcat container running a particular component or Vaadin application.

The API also allows you to open the default browser automatically and allocates an available HTTP port is none is provided. The API is extensible so that you can it you to your own needs.

# Building from Source

Clone the git repository using the URL on the Github home page:

    $ git clone git@github.com:bsblabs/vaadin-embed.git
    $ cd vaadin-embed

## Command Line

Use Maven, then on the command line:

    $ mvn install

Make sure you have access to the central repository.

## Usage

To use the Vaadin embed in your own projects, add the jar to your project's classpath or simply add the following depedency to your Maven project:

    <dependency>
        <groupId>com.bsb.common</groupId>
        <artifactId>com.bsb.common.vaadin.embed</artifactId>
        <version>0.1-SNAPSHOT
    </dependency>

To visualize a component that you have built, simply pass it to the `VaadinEmbed` builder.

    EmbedVaadin.forComponent(new Button("Hello")).start();

And if you want to open the default browser automatically at the proper URL, you could just add an extra parameter:

    EmbedVaadin.forComponent(new Button("Hello")).openBrowser(true).start();

You can as easily embed a Layout or a Window. Note that this mode is purely for development purpose as you pass an initialized component to the application. Multi-sessions are therefore not supported.

It is also possible to specify the `Class` of an `Application`. In that case, the server starts closer to the full thing.

## Configuration

There are two ways to change the default values of the embed server:

 - Through the presence of a `vaadin-embed.properties` file at the root of the classpath overriding the default values
 - By using the API

At this stage the following can be customized

 - `http.port`: the HTTP port the server listens to (an available port is taken by default)
 - `context.path`: the context of the generated web application (by default, the *root* context)
 - `context.rootDir`: the root directory of the generated web application to serve static files such as CSS and images
 - `server.await`: to block the thread that started the server (by default, *true*)
 - `vaadin.theme`: the vaadin theme to apply to the generated application. Not used if an application class is used (by default, *reindeer*)

And this showcase how the API can be used to customize these settings


        EmbedVaadin.forComponent(new Label("Hello World!"))
            .withContextPath("/foo")
            .withHttpPort(7070)
            .withContextRootDirectory("src/main/webapp")
            .withVaadinTheme("default")
            .openBrowser(true)
            .start();

