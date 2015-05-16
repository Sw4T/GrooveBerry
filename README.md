# GrooveBerry

##### **Développement d'une application Android pilotant une station musicale sur système embarqué**
* * *
**`Développeurs`**
* Jonathan BELMADI
* Pierre GAMBIER
* Nicolas SYMPHORIEN

**`SCRUM Master`**
* Natacha MORIN

**`Product Owner`**
* Enzo ALUNNI-BAGARELLI

* * *
**`Environnement`**

Nous avons utilisé l'IDE Eclipse pour développer notre serveur en JAVA et l'application Android avec le plug-in Android SDK. Par conséquent, notre git possède deux types de branches distinctes : `client` / `server` et la branche `master` fusionnant les deux projets.

**`Outils utilisés`**

Nous avons utilisé la RaspberryPi pour faire tourner notre serveur et un smartphone Android d'une version supérieure à la 2.2

* * *
__Configuration du projet__

**`Java`**
* Récupérer le projet `GrooveBerry_Serveur` sur la branche master et l'importer dans Eclipse
* Vérifier que les libraries soient bien inclus dans le Build Path (dossier /libs)
* Exécuter le fichier `Main` dans le package `network`, le serveur est lancé
* [OPTIONNEL] Exécuter le fichier `ClientTestMulti` dans le package `network`, ce client de test peut être exécuté plusieurs fois et interagit en ligne de commande avec le serveur

**`Android`**

`Configuration du SDK Android`

* Télécharger le [SDK Android] (http://developer.android.com/sdk/installing/index.html) et l'installer
* Sous eclipse, récupérer le plugin Android dans "Help -> Install New Softwares" avec l'adresse suivante :        
  https://dl-ssl.google.com/android/eclipse/
* Une fois eclipse relancé, lancer l'Android SDK Manager via l'icone présente dans la barre des tâches, télécharger un package contenant une API (testé sous l'API 21, version 5.0.1) ainsi que l'`Android Support Library` dans le dossier "Extras"
* Si vous n'avez pas de mobile Android compatible, vous pouvez gérer vos appareils virtuels via l'`AVD Manager` 

`Configuration du projet Android`

* Récupérer le projet `GrooveBerry_Client` via le répertoire git 
* Importer la bibliothèque `Android Support v7 appcompat` via "Import -> Android -> Existing Code" se situant dans le dossier `RACINE_INSTALLATION/android-sdks/extras/android/support/v7`
* Aller dans les propriétés du projet, vérifier que l'API que vous avez installé est bien la cible du projet sous "Android -> Project Build Target", ajouter la librarie `appcompat-v7` dans l'onglet "Library"
* Si des il y a des erreurs, vérifier dans les propriétés du projet appcompat-v7 que la case 'Library' est bien cochée dans l'onglet "Android"

`Lancement de l'application`

* Créer une nouvelle configuration Android pour pouvoir lancer le projet (via "Default Activity")
* Une fois l'application et le `Main` du serveur lancé, se connecter via le bouton des options (par défaut en haut à droite) et entrer l'adresse IP de la machine ou tourne le serveur
* Vous pouvez maintenant piloté la musique à distance !
