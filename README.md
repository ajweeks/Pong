Pong
====
A multiplayer 2D simplistic table tennis simulator

Technical Info
====
Messages that are sent over the network are one of two types:

    POSITION(y)  //for a postion change, y: the new y position
    INTERSECTION(y)  //for when the ball intersects a paddle, y: the y position where the intersection occured. Both clients caculate ball's new xv/yv independantly
