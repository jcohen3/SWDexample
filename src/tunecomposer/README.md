Project 6 Readme

CS 370, 10 May 2020

Ronan Byrne, Joshua Shin, Jesse Cohen, Tristan Chung

We used 2 stacks for the undo/redo functions. We added 2 methods that handled the undo and redo functions as well as a handful of functions to help with keeping track of if there are items in either stack. This was the only major change made to our project from the previous project.

Using the 2 stacks, handling the undo/redo functions was easy to manage. 

We could've sacrificed some time on the backend to make the organization of specific events easier to understand. One of the greatest challenges we encountered was finding a way to implement a stack that could handle multiple different actions (such as making notes, extending those notes, and creating groups). We thought about using a linked list but ultimately went with stacks of <Tune> (the interface Note and Group implement) objects. At the time, we knew this was not the best solution but didn't quite know how to implement it another way. As discussed below, I think there is a more elegant method that I now understand how to do but we simply ran out of time. 

We estimated about two story points: one for getting a rough draft of the undo/redo functions and another for testing boundary cases and debugging. However, we ended up having more than two story points. We ended up having an additional one or two story points to figure out how many stacks to implement and how to add multiple actions to them. The velocity for this project was slower than expected. Even though the last project had a lot more story points in comparison, we ended up having to halt all progress and make sure everything worked accordingly with the undo/redo functions.

Communication was probably our biggest struggle this project. We weren't able to meet as a team but instead had multiple 2 person meetings. Luckily, we made sure to keep everyone updated through text which helped with any confusion. For the future, we would probably start with a team meeting to discuss the plan for the next project and get a working outline of what we want. This way, we would'nt have to stop and explain our methods and instead update eachother on what was completed.

Our solution is not very elegant. We did not make it more elegant because we ran out of time. For this project specifically, it could be much more elegant if we created an "Action" interface and then used 2 stacks of those actions instead of saving and re-adding all notes and groups. We would than have classes for each action that implement the "Action" interface. The functions in the interface would most likely be limited to "Undo" and "Redo". 
The other components of the project that are not elegant include the many object and variable calls. Much of this was implemented before we discussed this concept in depth(I believe its called Law of Demeter where you in general avoid single lines with multiple function calls or object references on themselves). We spent a lot of time on the last project (project 5) organizing and figuring out fx:include which was super helpful from an organizational standpoint but not helpful with this specific issue. This can be fixed relatively easily by adding a handful of helper method.

If we made these two changes which are easy to do although a little time consuming, we believe they would put us in a good spot for the potential next project. 
We spent a lot of time on this project but were unable to find much time to discuss it together which certainly made things much slower than they could have been. 

The organization of controllers in our project, I feel is quite elegant and is a major plus to this project. 
