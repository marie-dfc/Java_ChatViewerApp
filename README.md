# ChatViewerApp

ChatViewerApp est une application Java simple qui permet de charger, afficher et modifier des conversations textuelles. 

---


## Structure du Code

### Classes Principales
1. **ChatViewerApp**  
   - Point d'entrée principal de l'application.
   - Responsable de la gestion des interactions utilisateur et des opérations principales (chargement des conversations, envoi de messages, etc.).

2. **Message**  
   - Représente un message individuel avec trois attributs :  
     - `timeStamp` : Horodatage du message.  
     - `nickName` : Nom d'utilisateur associé au message.  
     - `content` : Contenu du message.  
   - Méthodes principales :  
     - `getTimeStamp()`  
     - `getNickName()`  
     - `getContent()`

3. **MessageRenderer**  
   - Fournit une méthode pour afficher un message sous une forme lisible.  
   - Méthode principale :  
     - `renderMessage(Message message)`

4. **ConversationParser**  
   - Analyse les fichiers pour extraire les messages et les convertir en objets `Message`.  
   - Méthodes principales :  
     - `parseFile(File file)` : Analyse un fichier et retourne une liste de messages.  
     - `parseMessage(String textSection)` : Analyse un message brut et retourne un objet `Message`.

5. **InvalidMessageFormatException**  
   - Exception personnalisée pour signaler les erreurs lorsque le format d'un message est incorrect.

---

## Prérequis
- **Java JDK 11** ou version supérieure.
- Un éditeur ou IDE pour exécuter les fichiers Java (par exemple IntelliJ IDEA, Eclipse, ou VS Code).

---

## Installation et Exécution

### Étape 1 : Téléchargez le projet
Clonez ce dépôt ou téléchargez les fichiers source :
```bash
git clone https://github.com/votre-utilisateur/ChatViewerApp.git
cd ChatViewerApp
