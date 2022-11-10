//------------------------------------------------------------------------
// 2022 IT-ELAI Introduction to AI
// Topic : Informed Search Algorithms
//------------------------------------------------------------------------
//
// File Name    :   Node.java
// Class Name:  :   Node 
// Stereotype   :   
//
// Node class:
//  Methods:
//      +AddNeighbors                   - adds a neighbor to the current node
//  Utility:
//  Attributes:
//      +name(string)                   - text name of a place
//      +f(int)                         - holds the f(n)
//      +h(int)                         - holds the h(n)
//      +g(int)                         - holds the g(n)
//      +parent(Node)                   - pointer to the parent
//      +isVisited(bool)                - visited status
//      +neighbors(Linkedlist<Edge>     - list of node's edges/neighbors

//------------------------------------------------------------------------
// Notes:
//   Comment character code - UTF-8.
//------------------------------------------------------------------------
//  Change Activities:
// tag  Reason   Ver  Rev Date       Author      Description.
//------------------------------------------------------------------------
// $000 -------  0.1  001 2022-11-10 cabrillosa  First Release.
import java.util.*;

class Node implements Comparable<Node> //<-- needed to implement Comparable interface
{
    //---------------------------------------------------------------------
    // Attribute Definition.
    //---------------------------------------------------------------------
    //better to have getters and setters
    public String name;
    public int f;
    public int g;
    public int h;
    public Node parent;
    public boolean isVisited;
    public LinkedList<Edge> neighbors;

    public Node()
    {
        this.f = 0; //other sources prefers setting this to int.MAXVALUE
        this.g = 0;
        this.h = 0;
        this.isVisited = false;
        this.parent = null;
        this.neighbors = new LinkedList<Edge>();
    }

    //------------------------------------------------------------------------
    //  Method Name : AddNeighbors
    //  Description : adds a neighbor to the current node
    //  Arguments   : Node n
    //                int weight
    //  Return      : void
    //------------------------------------------------------------------------
    public void AddNeighbors(Node n, int weight)
    {
        Edge e = new Edge(n, weight);
        neighbors.add(e);
    }

    // Java PQ specific: Override compareTo function needed to correctly get the lowest weight.
    @Override
    public int compareTo(Node node)
    {
        if(this.h > node.h) {
            return 1;
        } else if(this.h < node.h) {
            return -1;
        } else {
            return 0;
        }
    }
}
//end of file