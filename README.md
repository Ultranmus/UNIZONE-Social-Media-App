# Unizone Social Media App Documentation

![Logo](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/unizone%20logo.jpg?alt=media&token=f5f7c5df-ab9b-4916-bdab-1f650f6b6a92)

## Working
Our feature-rich social media platform developed using Android Java and XML offers users a comprehensive and engaging experience. Users can connect and stay updated with others through the follow/follow back functionality, share posts with text, images, and videos, and engage in real-time chat conversations. The platform also includes the moments feature for sharing temporary content, liking posts, commenting, and replying. The integration of Firestore ensures instant updates without manual refresh. With a user-friendly interface and enhanced chat browsing capabilities, users can search for specific messages and difficult terms within the inbuilt browser. Overall, our platform provides seamless connectivity, content sharing, and interactive communication for an enjoyable social media experience.
- To better understand the features and how it works with image explanation, [please view this documentation](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/Unizone%20introduction.pdf?alt=media&token=cc8f6195-ed9c-4d38-b1ea-97a82847c8f5).
### Functionalities Implemented

#### Follow/Follow Back
The follow/follow back functionality allows users to establish connections and stay updated with the activities of the people they follow. Users can effortlessly follow other users within the platform, enabling them to build their network and expand their connections. This feature fosters user engagement and creates a sense of community within the platform.

#### Post Sharing
Our social media platform enables users to create and share posts with their connections. Users can compose posts with text, images, and videos, providing a versatile means of expression. We have implemented an intuitive interface that facilitates seamless content creation and formatting. Users have the option to add tags, location information, and privacy settings to their posts, enhancing the visibility and control over shared content.

#### Real-time Chat
We have integrated a powerful real-time chat feature that enables instant communication between users. Leveraging Firestore, messages are delivered in real-time, creating a fluid and interactive chat experience. Users can engage in one-on-one conversations or participate in group chats, fostering seamless communication across the platform. Additionally, users can share media files, view the online/offline status of their contacts, and benefit from read receipts, ensuring effective and timely communication.

#### Moments (Stories)
Similar to popular social media platforms, our platform includes the "Moments" feature, allowing users to share temporary content that disappears after a specified period. Users can upload photos or videos as moments, enhancing the interactive and dynamic nature of the platform. This feature encourages users to share fleeting moments and engage with timely and engaging content.

#### Liking Posts, Commenting, and Replying
To promote engagement and interaction, users can express their appreciation for posts by liking them. This feature encourages positive interactions and allows users to show support for the content shared by others. Additionally, users can provide comments on posts, facilitating discussions and conversations within the platform. To further enhance engagement, we have implemented a reply functionality that enables users to respond to specific comments, promoting meaningful interactions and dialogue.

#### Firestore Integration
To ensure a seamless and real-time experience, we have integrated Firestore, a robust real-time database solution offered by Google Cloud Platform. Firestore allows us to sync data across devices in real-time, ensuring that users receive instant updates without the need for manual refresh. This feature significantly enhances the user experience, keeping users informed about the latest activities and facilitating uninterrupted engagement within the platform.

#### User-Friendly Interface
We have prioritized user experience by designing a user-friendly interface that promotes ease of use and navigation. Our interface incorporates intuitive icons, clear labels, and organized layouts, enabling users to quickly access and utilize various features within the platform. In particular, we have focused on enhancing browsing capabilities within the chat module.

#### Integration of Inbuilt Browser
Within the chat module of our social media platform, we have integrated an inbuilt browser component that emulates the functionality of popular web browsers like Chrome. This browser component allows users to search the internet without leaving the app.

#### Search Functionality
To enhance the functionality of searching for difficult terms within the chat module, we have incorporated a search bar or search icon within the chat interface. This feature provides users with a convenient way to input specific terms or difficult abbreviations they want to search for. When users enter their search query, the platform's search functionality scans the chat messages and conversations to identify any instances of the searched terms. The search results are then displayed, highlighting the relevant messages or conversations where the terms appear.

#### Launching the Inbuilt Browser
When a user initiates a search, we capture the search query and pass it to the inbuilt browser component. The inbuilt browser then launches and displays the search results page obtained from a search engine, similar to how Chrome or other web browsers would function.

#### Browsing Search Results
Within the inbuilt browser interface, users can navigate through the search results, click on links, view web pages, and access external resources related to their search query. This allows them to find explanations or definitions for difficult terms, improving their understanding within the context of the chat conversation.

#### Seamless Transition
To ensure a seamless user experience, we provide a convenient way for users to return to the chat module after they have finished searching. This can be achieved by including a back or close button within the inbuilt browser interface. When users click on this button, they are taken back to the chat module, where they can continue their conversation.

#### Google Mic Option
To facilitate voice input and enhance user convenience, we have integrated the Google Mic option within the inbuilt browser. Users can tap on the microphone icon located within the browser interface to activate voice search. This enables users to perform searches by speaking their queries instead of typing them.

#### Desktop Mode
We understand that some websites offer a different layout or functionality when accessed from a desktop browser. To provide users with a versatile browsing experience, we have included a desktop mode feature within the inbuilt browser. Users can toggle between mobile and desktop modes, allowing them to view websites as they would appear on a desktop browser. This ensures compatibility with websites that have responsive designs optimized for desktop screens.

#### Normal Mode and Desktop Mode Switch
Within the browser interface, we provide a user-friendly option for users to switch between normal mode and desktop mode. This can be achieved through a toggle button or a menu option. When users activate desktop mode, the inbuilt browser will display web pages in a format similar to that of a desktop browser. Conversely, when users switch back to normal mode, the browser will display web pages in the default mobile view. By incorporating the Google Mic option, desktop mode, and the ability to switch between normal mode and desktop mode, we provide users with a comprehensive browsing experience within our social media platform. These features enhance user convenience, enable voice search capabilities, and ensure compatibility with various websites that have different layouts or functionalities based on the browsing device.

In conclusion, our social media platform, developed using Android Java and XML, offers users a comprehensive and engaging experience. With features such as follow/follow back, post sharing, real-time chat, moments, liking posts, commenting, and replying, users can connect, share content, and interact with others. Leveraging Firestore, we provide real-time updates without the need for manual refresh, creating an immersive and dynamic environment. Our user-friendly interface and advanced chat browsing capabilities enhance the overall user experience. Our platform aims to seamlessly connect users while prioritizing usability and functionality, ensuring a unique social media experience.

## App Interface

### App Logo and Splash Screen
On the left side is the app logo displayed in the app menu, representing our application. On the right side, the splash screen with an engaging animation greets users upon launching the app. This screen serves as a loading interface and provides visual confirmation of app activity. The splash screen balances captivating visuals with a smooth transition to the main app interface. It plays a crucial role in enhancing the overall user experience.

### Sign Up and Login Screen
On the left side, the sign-up screen not only requires users to enter their email and password but also enables them to proceed with the sign-up process, unlocking the app's features. Meanwhile, the login screen on the right ensures that returning users can effortlessly access the app's home screen by providing their email and password and clicking "Login." These screens prioritize user authentication and convenience, facilitating a seamless and personalized app experience for users.

### Posts Tab
After signup or login, the left screen showcases posts from connected users, allowing likes and comments. On clicking the comment icon, the post screen opens. Pagination loads new content on scroll, with a "No more posts" toast at the bottom. Real-time updates seamlessly add new posts at the top, preserving user visibility and position. This engaging platform encourages interaction and ensures a smooth browsing experience, fostering a vibrant and dynamic community.

### Post Screen
On the left side, the post screen showcases a posted image along with the username and profile picture of the user who made the post. It also displays information about the user, such as their bio. Additionally, the post screen indicates the number of comments and likes received by the post. Users can actively engage by writing comments on the post, and they have the option to reply to specific comments by clicking the "reply" button, as illustrated in the image on the right side, which demonstrates multiple replies on a comment.

### Sign-up Process
The left image showcases the sign-up screen, where users input their desired email and password. After clicking "Sign Up," they are directed to the username screen as shown on the right side image. Here, users must select a unique username that is unavailable to other users. This process ensures individuality and sets each user apart within the app's community. By choosing a username, users personalize their accounts and establish a distinct identity for themselves.

### App Interface after Sign-up: A Snapshot of User Experience
On the left side, the home screen is displayed with an open drawer that offers users access to a range of features, such as searching for users, creating posts, engaging in chat conversations with OpenAI's ChatGPT, and logging out. Meanwhile, the right side showcases the home screen, where posts from other users are typically shown. As the user has recently signed up and has yet to establish connections, no posts are currently visible on the screen.

### Navigation Drawer and its Features
On the left side, the home screen is displayed with an open drawer that allows users to access various features such as searching for users, creating posts, ChatGPT assistance, and logging out. On the right side, the ChatGPT screen is shown, which opens upon selecting the ChatGPT option from the navigation drawer. In this screen, users can enter any question or query, and ChatGPT provides assistance by generating a response. The response is presented in a dialogue box, and users can copy the text by performing a long click on the response.

### Create Post and Search Features
On the left side, the create post screen is presented, allowing users to compose and publish their posts. They have the option to attach text to images, enhancing their content with captions or descriptions before sharing it with others.

On the right side, the search screen enables users to search for other users by their username. As users start typing, suggestions of users with similar surnames or usernames appear, simplifying the search process. By clicking on a suggested user from the list, our user is directed to the selected user's profile, providing a seamless way to explore and connect with others.

### How a Connection is Established?
After searching for a user, the left side displays their profile, including profile picture, username, and information about connections and posts. On the right side of the screen, a follow action can be initiated by clicking the "+" button located at the top-right corner. Upon clicking, a buffering animation occurs, indicating that the follow action is being processed. Once the action is completed successfully, a toast notification with the message "Followed" is shown, confirming that the user has been followed.

### Like, Comment & Reply on Post
On the left side of the screen, there is a post that users can interact with by liking it. By clicking the thumbs-up button, the post is liked, and the button color changes to red. Additionally, the like count increases by 1, and a toast notification saying "Liked" is displayed to confirm the action. On the right side, users can engage with the post by entering comments. They can click on the "Enter Comment" field and type their thoughts or feedback regarding the post.

After composing the comment, they can click the "Send" button to submit it. Once the comment is successfully submitted, it will be displayed below the post, allowing others to view and respond to it. A toast notification will appear, confirming the user's action with a message like "Commented." Moreover, to foster more engaging conversations, each individual comment is equipped with a convenient "Reply" button. By clicking on the "Reply" button associated with a specific comment, users can compose and submit multiple replies directly related to that particular comment. This threaded approach enables users to engage in in-depth discussions, respond directly to others, and create a more interactive and interconnected comment section.

### Chats & Personal Chat Screen
On the left side is the chat tabs displaying various existing chats that the user has had with their connections. This tab represents different chat threads and provides easy access to previous chats. Below the chat tabs, there is a plus button (+) that allows users to create a new chat. Clicking on this button will direct the user to a new screen where a list of their connections is shown. From this list, the user can select any connection they want to initiate a chat with. Upon selecting a connection, the personal chat screen will open for the user to start a conversation with the chosen connection.

Upon selecting a connection, the personal chat screen will open, providing a dedicated space for the user to start a conversation with the chosen connection. If there is already an existing chat between the user and the selected connection, the chat history will be displayed, showing the previous messages exchanged. However, if there is no existing chat between the user and the selected connection, an empty chat screen will be shown, indicating that no previous conversation has taken place. The user can then begin a new conversation by sending the first message.

### How to Browse in Chat?
On the left side, there is an option to browse using an in-built browser. Users can enable it by clicking the search icon in the top-right corner of the chat interface. They can directly enter a URL or click on a chat message to populate the search text field. Clicking the search button opens a new screen, enabling users to browse the web within the application. This feature allows convenient access to websites, information, and online content without leaving

 the chat interface.

After clicking the search button, the in-built browser will open with various features. Users can explore web pages and access different functionalities within the browser interface. To view a webpage in desktop mode, users can select the "Desktop Mode" option from the menu located in the top-right corner of the browser interface. This allows the webpage to be displayed as it would appear on a desktop computer, providing a wider view and potentially enabling access to additional features. If users wish to return to the chat interface, they can simply click on the home button or navigate back to the chat by using the designated navigation options within the browser.

### How to Create Moments?
In the left figure, there is a "Moments" tab that displays shared memories with connections, which disappear after 1 day. By clicking the plus button on the profile image, users can navigate to another screen specifically designed for creating new moments. On this screen, a prominent big plus icon is displayed, indicating that it can be clicked to initiate the creation of moments with their connections. This feature allows users to capture and document special moments, fostering a sense of shared experiences and memories within the platform.

In the left figure, after clicking the plus button from the previous screen, users are presented with options to select a video for their moment. They can choose a video from their device's gallery. Once a video is selected, users can provide a title or caption for the moment to add context or describe the content. After entering the title, users can create the moment by simply clicking the send button. The system will process and buffer the moment, ensuring it is ready for viewing and sharing. Once the moment is successfully created, users will be guided back to the "Moments" tab, where they can view and share their newly created moment with their connections.

### Screenshots

![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/1.jpg?alt=media&token=2dbfbae0-6f7a-4f51-bd8c-6cb1e1edca51)


![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/2.jpg?alt=media&token=dc7ba3f3-6589-4e3a-9af9-234ebe865f28)


![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/3.jpg?alt=media&token=599b1dd0-a762-46f0-b28f-2cbb673af7be)


![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/4.jpg?alt=media&token=2a286a81-97bd-4785-9bd2-66ea22b8e616)


![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/5.jpg?alt=media&token=4ad5e884-b8ea-444f-84c5-4e3fd39a4d23)


![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/6.jpg?alt=media&token=8a05b6d9-8937-45b1-afb6-2c56d6d17a83)


![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/7.jpg?alt=media&token=50ffc847-9a9a-4a83-b209-9abdea18198d)


![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/8.jpg?alt=media&token=e2445d5d-a488-4c10-be85-d7c970748658)


![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/9.jpg?alt=media&token=e5d96db5-3b68-46e1-9607-044316608721)


![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/10.jpg?alt=media&token=b8ca2eea-b2ff-4488-8750-e19b09e42cad)


![](https://firebasestorage.googleapis.com/v0/b/github-895c7.appspot.com/o/11.jpg?alt=media&token=409d85d5-ddc4-45cf-be27-5ee422c1a5fb)


### Contact
For any inquiries or assistance, please feel free to contact at hashmack17@gmail.com. We are here to help you with any questions or issues you may have while using the Unizone app.
