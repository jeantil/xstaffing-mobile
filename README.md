Projet XStaffing-mobile
==========
### Prérequis
Le strict minimum est d'avoir une jvm, sbt 0.11.3, un clone du projet et un éditeur de texte.
Quelque soit votre mode de développement préféré il faut commencer par lancer `sbt` depuis le dossier server du workspace et mettre à jour les dépendances :

    $ sbt
    [info] Loading global plugins from D:\programs\Java\sbt\plugins
    [info] Loading project definition from D:\devel\perso\xstaffing\project
    [info] Set current project to xstaffing (in build file:/D:/devel/perso/xstaffing/)
    sbt (xstaffing)> update
    sbt (xstaffing)> update-classifiers
La seconde commande est optionnelle mais permet de télécharger les sources et les javadocs (pour les artéfacts qui les ont publiés).

### Lancer la console play
Ca ne sert a rien mais ça affiche le logo :) Depuis la racine du projet lancez `sbt play` :

    $ sbt play
    [info] Loading global plugins from D:\programs\Java\sbt\plugins
    [info] Loading project definition from D:\devel\perso\xstaffing\project
    [info] Set current project to xstaffing (in build file:/D:/devel/perso/xstaffing/)
           _            _
     _ __ | | __ _ _  _| |
    | '_ \| |/ _' | || |_|
    |  __/|_|\____|\__ (_)
    |_|            |__/

    play! 2.0, http://www.playframework.org

    > Type "help play" or "license" for more information.
    > Type "exit" or use Ctrl+D to leave this console.

    sbt (xstaffing)>

### Configurer un IDE
Play supporte en natif la configuration des deux plux gros IDE java : [Eclipse (ScalaIDE)](http://scala-ide.org/download/current.html), [IntelliJ (Community suffit)](http://confluence.jetbrains.net/display/IDEADEV/IDEA+11.1+EAP). Le détail des instructions pour eclipse est là http://www.playframework.org/documentation/2.0/IDE , en court il s'agit de lancer la console play et d'utiliser `eclipsify with-source=true`

    sbt (xstaffing)> eclipsify with-source=true
    [info] Compiling 7 Scala sources and 1 Java source to D:\devel\perso\xstaffing\target\scala-2.9.1\classes...
    [warn]        _            _
    [warn]  _ __ | | __ _ _  _| |
    [warn] | '_ \| |/ _' | || |_|
    [warn] |  __/|_|\____|\__ (_)
    [warn] |_|            |__/
    [warn]
    [warn] play! 2.0, http://www.playframework.org
    [info] ...about to generate an Intellij project module(SCALA) called xstaffing.iml
    [warn] xstaffing.iml was generated
    [warn] If you see unresolved symbols, you might need to run compile first.
    [warn] Have fun!
    [success] Total time: 9 s, completed 22 mars 2012 13:49:27
    sbt (xstaffing)>

Pour IntelliJ, c'est tout aussi simple avec `idea with-sources` :

    sbt (xstaffing)> idea with-sources
    [info] Compiling 7 Scala sources and 1 Java source to D:\devel\perso\xstaffing\target\scala-2.9.1\classes...
    [warn]        _            _
    [warn]  _ __ | | __ _ _  _| |
    [warn] | '_ \| |/ _' | || |_|
    [warn] |  __/|_|\____|\__ (_)
    [warn] |_|            |__/
    [warn]
    [warn] play! 2.0, http://www.playframework.org
    [info] ...about to generate an Intellij project module(SCALA) called xstaffing.iml
    [warn] xstaffing.iml was generated
    [warn] If you see unresolved symbols, you might need to run compile first.
    [warn] Have fun!
    [success] Total time: 9 s, completed 22 mars 2012 13:49:27
    sbt (xstaffing)>

### Les commandes sbt utiles :

- `update` met à jour les dépendances
- `compile` compile l'application
- `test` joue les tests unitaires
- `~ [command]` joue [command] en continu (compile et test sont de bonnes idées)
- `run` lance l'application


License
--------

See LICENSE file.
