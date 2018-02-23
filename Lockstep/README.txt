The Lockstep Framework offers a variety of functionnalities to easily create Entities and Systems that will update them.
It's heavily inspired by the ECS (Entity Component System) Framework.

The ECS vision:
- Engine :
    The engine is responsible of the top level of the API. It handles all the underlying logic behind the updates
    The engine contains 2 types of objects:
        - Components:
            a component is essentially a data container. It holds a small pack of data that share the same aspect.
        - Entities:
            an entity is a group of components that may change over time. It has an identity (ID) to be identified when there are multiple entities of the same Profile.
        - Systems:
            Systems are the rules of the simulation. They apply on certain Entity profiles and will update them every step.

Hard constraints and trades-off:
    This Lockstep is designed for parallelisation and networking fiability. Therefor, there are some hard constraints and good practice one should be aware of.
    -Systems and IComponents are limited by the strictfp keyword to allow the use of floating point to replicate Lockstep simulations over a network on multiple clients.
    -Systems may destroy entities, however they might still be updated by later Systems until the update end. This is due to the immutability of the entity array
    -Systems can process entities in parallel and in an unordered maner. This implies that they should avoid to be stateful regarding a particular Entity. However it's common practice to use Marker components to bring specific behaviors to specific entities.
