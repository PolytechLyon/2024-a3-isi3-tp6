# Simulation de Vélo
Dans ce TP, vous réalisez des exercices sur les **patrons de conception**[^1] à l'aide d'une application simple qui simule, d'une manière très approximative, le fonctionnement d'un vélo.   

## Modalité
* Ces travaux pratiques sont notés.
* Ils sont à réaliser en binôme.
* Le compte rendu à remplir se trouve [ici](Rapport.md).

## Véhicule composite
Dans notre simulation, comme dans la vie réelle, un véhicule est un objet mobile, ses composants le sont aussi.
Un objet mobile a une vitesse qui pourrait être nulle, ainis qu'une masse.
La vitesse du véhicule est calculée en fonction de la moyenne pondérée des vitesses de ses composants.
La masse, quant à elle, est la somme des masses des composants.

Observez le diagramme de classe partiel suivant qui représente les relations entre les types `MobileObject` et `Vehicle`.

![Diagramme des classes Mobile et Vehicle](images/cd-mobile-bike-wheel.png "Diagramme des classes Mobile et Vehicle")

> **Exercise 1** 
>
> Quel patron de conception reflète ce modèle ? Identifiez le rôle de chaque de ces deux classes dans ce patron.
> 
> Réalisez la classe `TagAlongBike` qui représente un vélo parental traînant un vélo d'enfant (voir image).
> Le vélo parental est un vélo qui contient dans ses composants un autre vélo simple.
> Doit-on récrire la méthode `getVelocity()` ou la méthode `getMass()` pour la nouvelle classe ?

![Vélo parental](images/tag-along-bike.png "Un vélo parental contient dans ses composants un autre vélo simple")

## Parcours véloce
Dans la méthode `getVelocity()` de la classe `Vehicule` on parcourt tous les composants d'un véhicule pour calculer sa vitesse.

> **Exercise 2**
>
> Quel patron de conception utilise la méthode `getVelocity()` pour parcourir les composants d'un véhicule ? 
> Quel avantage apporte ce patron ?
> 
> Modifier la structure de donnée utilisée pour stocker les composants d'un véhicule de `Set` à `List`.
> Doit-on modifier la réalisation de la méthode `getVelocity()` ?

## Simulation simultanée
La simulation est réalisée à un instant précis. Cet instant est détérminé par la méthode `getTime()` de la classe `Clock` qui représente une horloge.
À l'état actuel, le calcul de vitesse montre un problème dans les journaux d'événements[^2].
Chaque roue calcule sa vitesse à un instant généralement différent des autres roues.
Cela fait que les roues n'ont pas toujours la même vitesse. Un problème de synchronisation.

À présent, chaque roue a sa propre horloge, une instance de `Clock`, ce qui fait que la simulation est désynchronisée et irréaliste.

On n'a pas ce problème-là dans la vraie vie : le temps est un concept absolu à l'échelle de la vitesse d'un vélo.
La simulation doit donc s'exécuter au même instant pour tous les composants.
Il faudra veiller à ce que toutes les roues aient la même référence temporelle, ou la même horloge.

> **Exercise 3**
> 
> Utilisez le patron de conception de création **singleton** pour assurer qu'il n'y a qu'une seule instance de la classe `Clock` dans l'application.
> Les deux roues doivent donc utiliser cette même instance et les vitesses doivent être homogènes.
> 
> Expliquez, en quelques lignes, les étapes de la réalisation de ce patron dans le code.
> 
> Attention, seulement une seule instance doit être instantiable de la classe `Clock`.

## Dépendance cycliste
Observez les classes `Bike` et `Wheel`.
À l'état actuel, un vélo a des références à ses roues.
Une roue a une référence au vélo pour obtenir l'effort appliqué, nécessaire pour le calcul de sa vitesse.     

> **Exercise 4**
> 
> Les classes `Bike` et `Wheel`, appartiennent-elles au même paquetage[^3] ?
> Quel type de dépendance y a-t-il entre les deux classes ?
> Cette dépendance adhère-t-elle aux bonnes pratiques de conception ?
>
> Quelle fonctionnalité de la classe `Bike` utilise la classe `Wheel` ?
> Y a-t-il déjà une abstraction de la classe `Bike` qui isole cette fonctionnalité ?
> Dans quel paquetage se trouve cette abstraction ?
> 
> Proposez, et réalisez, une solution pour casser la dépendance cyclique entre les classes `Bike` et `Wheel`.

## Journaux nommés
Pour tracer les sources des messages dans les journaux, on utilise des journaux nommés, représentés par la classe `NamedLogger` dans notre code.
Un journal nommé a un nom, passé en paramètre au constructeur. Chaque message tracé par un tel journal est précédé par ce nom.
`FileLogger` et `ConsoleLogger` sont deux classes qui héritent de `NamedLogger`.

Leurs méthodes `log()` partagent une étape commune : la fabrication du message final à partir de nom du journal, format et arguments du message.
Cette fonctionnalité commune se matérialise avec un code en doublon à l'état actuel.

> **Exercise 5**
> 
> Utiliser le patron de conception **patron de méthode**[^4] pour centraliser cette étape commune à un seul endroit et d'éviter le code en doublon.
> 
> Modifiez la classe `NamedLogger` et ses sous-classes pour réaliser ce patron.

## Journaux centralisés
Les journaux d'événements sont utilisés à plusieurs endroits dans notre code : la classe `Vehicule`, la classe `Wheel` et la classe `BikeSimulator`.
Malheureusement, toutes les classes n'utilisent pas la même réalisation de l'interface `Logger`.
À présent, pour lire tous les messages des journaux, on est obligés de regarder et dans la sortie standard et dans le fichier `log.txt`.

On voudrait donc centraliser le choix de la réalisation de l'interface `Logger` à un seul endroit. 

> **Exercise 6**
> 
> Utiliser le patron de conceptions **méthode de fabrique** pour centraliser le choix de réalisation de l'interface `Logger`.
> 
> C'est quoi la difference entre cette méthode et le patron singleton ?

## Journaux avec suivi du temps
On souhaite faire en sorte que les journaux, peu importe leurs types, tracent à côte de chaque message l'heure précise à laquelle le message a été produit.

> **Exercice 7**
>
> Appliquez le patron de conception **décorateur** pour créer la classe `TimestampedLoggerDecorator` qui permet d'ajouter l'heure au message d'un journal d'événement.
> La nouvelle fonctionnalité devra marcher peu importe le type de journal (console ou fichier).

## Injection de dépendance
On souhaite lancer la simulation de vélo soit avec un vélo simple (classe `SimpleBike`), soit avec un vélo parental (classe `TagAlongBike`).

Pour ce faire, on utilise le principe d'injection de dépendance.
Cela implique que le code client n'instancie pas directement les objects dont il a besoin, mais que le contexte de l'application les injecte.
Il suffit donc que le code applicatif déclare son besoin.

L'édition standard de Java (Jave SE) fournit un outil simple pour l'injection de dépendance, matérialisé par la classe [ServiceLoader](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html).
La classe `Context` dans le code est une classe utilitaire qui propose une interface simplifiée de `ServiceLoader`, avec un ensemble restreint de ses fonctionnalités.

Observez le fichier [`fr.polytech.sim.cycling.Bike`](src/main/resources/META-INF/services/fr.polytech.sim.cycling.Bike) dans le dossier `src/main/resources/META-INF/services` qui sert à définir quelles sous-classes il faut injecter quand un vélo (un objet de type `Bike`) est attendu par l'application.

> **Exercice 8**
> 
> Quel patron de conception suit la classe `Context` vis-à-vis l'outil `ServiceLoader` ? 
> 
> Utilisez la classe utilitaire `Context` pour injecter un objet de type `Bike` dans la simulation, au lieu de l'instancier avec le mot clef `new`.
> Changez la classe injectée de `SimpleBike` à `TagAlongBike`.
> 
> Peut-on avoir plusieurs lignes dans le fichier `fr.polytech.sim.cycling.Bike` ?
> À quoi correspond chaque de ces lignes ?

On souhaite maintenant que notre application puisse simuler plusieurs vélos.

Notez que l'outil `ServiceLoader` supporte l'injection de plusieurs objets.
La méthode souche `injectAll()` de la classe `Context` a pour objectif d'injecter plusieurs objets d'un type donné, si cela a été correctement configuré. 

> **Exercice 9**
> 
> Observez le type de retour de la méthode `injectAll()`.
> Quel patron de conception propose cette méthode pour parcourir tous les objets d'un type donné disponibles dans le contexte applicatif ?
> 
> Réaliser cette méthode en cohérence avec son objectif décrit ci-dessus et détaillé dans sa documentation.
> 
> Modifiez la simulation de vélo pour utiliser cette même méthode afin de simuler tous les types de vélo presénts dans la configuration.
> Modifiez la configuration pour simuler et `SimpleBike` et `TagAlongBike`.


[^1]: *Design Patters* en anglais.
[^2]: *Logs* en anglais.
[^3]: *Package* en anglais.
[^3]: *Template method* en anglais.

