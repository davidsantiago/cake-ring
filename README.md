# Cake-Ring

Control Ring web apps during development with Cake. 

# Introduction

This is a Cake port of part of the functionality that James Reeves's excellent [lein-ring](http://github.com/weavejester/lein-ring) provides. Currently the only functional commands are `cake ring server`, which starts a development server according to the `:ring` key in your project, and `cake ring stop`, which shuts down any server that you may have started. 

Just like lein-ring, you specify your main Ring handler with a `:ring` key in your project. The `:ring` key should be a map containing at least a `:handler` key, which gives the fully qualified name of your top level Ring handler. You can also specify `:init` and `:destroy`, which are two functions that will be called on app start and app shutdown. 

Currently this is the only implemented functionality, but in the future this may expand to include the ability to package up Ring webapps into wars automatically. 

# Obtaining it

Just add
    [cake-ring "0.1.0-SNAPSHOT"]
to your project.clj and then do `cake deps`.
