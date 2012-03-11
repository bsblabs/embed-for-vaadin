# Vaadin embed

The Vaadin embed is a library allowing you to quickly initialize Vaadin from your IDE. It provides an easy-to-use API to initialize an embed Tomcat container running a particular component or Vaadin application.

The API also allows you to open the default browser automatically and allocates an available HTTP port is none is provided. The API is extensible so that you can suit you to your own needs.

# Building from Source

Clone the git repository using the URL on the Github home page:

    $ git clone git@github.com:bsblabs/vaadin-embed.git
    $ cd vaadin-embed

## Command Line

Use Maven, then on the command line:

    $ mvn install

Make sure you have access to the central repository.

## Usage

To use the embedded in your own projects, add the jar to your project's classpath or simply add the following dependencies to your Maven project:

    <dependency>
        <groupId>com.bsb.common</groupId>
        <artifactId>com.bsb.common.vaadin.embed</artifactId>
        <version>0.1-SNAPSHOT
    </dependency>

To visualize a component that you have built, simply pass it to the 'VaadinEmbed' builder.

    EmbedVaadin.forComponent(new Button("Hello")).start();

You can as easily embed a Layout or a Window. Note that this mode is purely for development purpose as you pass an initialized component to the application. Multi-sessions are therefore not supported.

It is also possible to specify the 'Class' of an 'Application'. In that case, the server starts closer to the full thing.