//------------------------------------------------------------------------
// 2022 IT-ELAI Introduction to AI
// Topic : Informed Search Algorithms
//------------------------------------------------------------------------
//
// File Name    :   Edge.java
// Class Name:  :   Edge 
// Stereotype   :   
//
// Edge class:
//  Methods:
//  Utility:w
//  Attributes:
//      +weight(int)         - Weight from the parent node.
//      +node(Node)          - Node connected to the parent

//------------------------------------------------------------------------
// Notes:
//   Comment character code - UTF-8.
//------------------------------------------------------------------------
//  Change Activities:
// tag  Reason   Ver  Rev Date       Author      Description.
//------------------------------------------------------------------------
// $000 -------  0.1  001 2022-11-10 cabrillosa  First Release.
import java.util.*;

class Edge{
    //---------------------------------------------------------------------
    // Attribute Definition.
    //---------------------------------------------------------------------
    //should have getters and setters
    int weight;
    Node node;

    //------------------------------------------------------------------------
    //  Method Name : Edge
    //  Description : Constructor. Initialize the need attributes.
    //  Arguments   : Node n
    //                int weight
    //  Return      : void.
    //------------------------------------------------------------------------
    public Edge(Node node, int weight)
    {
        this.node = node;
        this.weight = weight;
    }
}
//end of file