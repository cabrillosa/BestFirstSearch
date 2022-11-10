//------------------------------------------------------------------------
// 2022 IT-ELAI Introduction to AI
// Topic : Informed Search Algorithms
//------------------------------------------------------------------------
//
// File Name    :   GraphTraversal.java
// Class Name:  :   GraphTraversal 
// Stereotype   :   
//
// GraphTraversal class:
//  Methods:
//      +add_place                       - Add a place in string format.
//      +connect                         - Connect one vertex to another vertex.
//      +displayAdjacencyList            - Display adjacency list.
//      +GreedyBestFirstSearch           - Traverse the map using Greedy best first search
//      +astar                           - Traverse the map using A*
//  Utility:
//      -getNodeByName                   - search the map using the string name
//      -reconstruct_path                - reconstruct the solution/path
//      -getLowestFscore                 - get the lowest f(n) in the a list of Node
//  Attributes:
//      -graph(LinkedList<Node>)         - Number of places/vertices in the map.

//------------------------------------------------------------------------
// Notes:
//   Comment character code - UTF-8.
//------------------------------------------------------------------------
//  Change Activities:
// tag  Reason   Ver  Rev Date       Author      Description.
//------------------------------------------------------------------------
// $000 -------  0.1  001 2022-11-10 cabrillosa  First Release.
import java.util.*;

class GraphTraversal
{
    //---------------------------------------------------------------------
    // Attribute Definition.
    //---------------------------------------------------------------------
    private LinkedList<Node> graph;

    //------------------------------------------------------------------------
    //  Method Name : GraphTraversal
    //  Description : Constructor. Initialize the need attributes.
    //  Arguments   : void.
    //  Return      : void.
    //------------------------------------------------------------------------
    public GraphTraversal()
    {
        //initialize the graph
        graph = new LinkedList<Node>();
    }

    //------------------------------------------------------------------------
    //  Method Name : add_place
    //  Description : Adds a place in string format and h(n) straight line distance.
    //  Arguments   : String place
    //                int h (h param should be pre-computed!)
    //  Return      : void
    //------------------------------------------------------------------------
    public void add_place(String place, int h)
    {
        Node n = new Node();
        n.name = place;
        n.h = h;
        graph.add(n);
    }

    //------------------------------------------------------------------------
    //  Method Name : connect
    //  Description : Connect one vertex to another vertex with specified weight
    //  Arguments   : string v1
    //                string v2
    //                int dist
    //  Return      : 0 (OK)
    //               -1 (NG - place is not in the list)
    //------------------------------------------------------------------------
    public int connect(String place1, String place2, int dist)
    {
        Node n1 = getNodeByName(place1);
        Node n2 = getNodeByName(place2);

        if (n1 == null || n2 == null)
        {
            return -1;
        }

        n1.AddNeighbors(n2, dist);
        n2.AddNeighbors(n1, dist);

        return 0;
    }

    //------------------------------------------------------------------------
    //  Method Name : displayAdjacencyList
    //  Description : Display adjacency list.
    //  Arguments   : None.
    //  Return      : None.
    //------------------------------------------------------------------------
    public void displayAdjacencyList()
    {
        Iterator<Node> ite = graph.iterator();

        while (ite.hasNext())
        {
            Node n = ite.next();
            System.out.print(n.name +"::");
            Iterator<Edge> edges = n.neighbors.iterator();

            while(edges.hasNext())
            {
                Edge e = edges.next();
                System.out.print(e.node.name + " -> ");
            }
            System.out.println(); 
        }
    }

    //------------------------------------------------------------------------
    //  Method Name : GreedyBestFirstSearch
    //  Description : Traverse the map using Greedy Best First Search
    //  Arguments   : String start_place
    //                String goal_place
    //  Return      : Void
    //------------------------------------------------------------------------
    public void GreedyBestFirstSearch(String start_place, String goal_place)
    {
        Node start = getNodeByName(start_place);
        PriorityQueue<Node> pq = new PriorityQueue<Node>();

        if(start == null)
        {
            System.out.println("Node not found!");
            return;
        }

        start.f = start.h;
        start.isVisited = true;
        pq.add(start);

        while(pq.size() > 0)
        {
            Node current = pq.poll();
            current.isVisited = true;
            if(current.name == goal_place)
            {
                //we reached the goal
                reconstruct_path(current);
                return;
            }

            Iterator<Edge> ite = current.neighbors.iterator();
            while(ite.hasNext())
            {
                Edge e = ite.next();
                if(e.node.isVisited != true)
                {
                    Node temp = e.node;
                    temp.f = temp.h;
                    temp.parent = current;
                    pq.add(temp);
                }    
            }
        }
        System.out.println("No path to goal!");
    }

    //------------------------------------------------------------------------
    //  Method Name : astar
    //  Description : Traverse the map using A*
    //  Arguments   : String start_place
    //                String goal_place
    //  Return      : Void
    //------------------------------------------------------------------------
    public void astar(String start_place, String goal_place)
    {
        Node start = getNodeByName(start_place);

        if (start == null)
        {
            System.out.println("Node not found!");
            return;
        }

        LinkedList<Node> openlist = new LinkedList<Node>();
        LinkedList<Node> closedlist = new LinkedList<Node>();

        start.f = start.g + start.h;
        openlist.add(start);

        while(openlist.size() > 0)
        {
            Node current = getLowestFscore(openlist);

            if(current.name == goal_place)
            {
                System.out.println(current.f + " is the final F score!");
                reconstruct_path(current);
                return;
            }

            Iterator<Edge> neighbor = current.neighbors.iterator();

            while(neighbor.hasNext())
            {
                Edge m = neighbor.next();
                int g_so_far = current.g + m.weight;

                if(!closedlist.contains(m.node) && !openlist.contains(m.node))
                {
                    m.node.parent = current;
                    m.node.g = g_so_far;
                    m.node.f = m.node.g + m.node.h;
                    openlist.add(m.node);
                }
                else
                {
                    if(g_so_far < m.node.g)
                    {
                        m.node.parent = current;
                        m.node.g = g_so_far;
                        m.node.f = m.node.g + m.node.h;

                        if (closedlist.contains(m.node))
                        {
                            openlist.add(m.node);
                        }
                    }
                }
            }
            openlist.remove(current);
            closedlist.add(current);
        }
        System.out.println("No path to goal!");
    }

    // ----------------------
    //  UTILITY FUNCTIONS
    //-----------------------

    //------------------------------------------------------------------------
    //  Method Name : getNodeByName
    //  Description : get the node by its name
    //  Arguments   : String name
    //  Return      : Node, if name is found
    //                null, if name is not found
    //------------------------------------------------------------------------
    private Node getNodeByName(String name)
    {
        Iterator<Node> ite = graph.iterator();

        while(ite.hasNext()) //n = n.next
        {
            Node n = ite.next();

            if(n.name == name)
            {
                //String is in the graph, return!
                return n;
            }
        }
        return null;
    }

    //------------------------------------------------------------------------
    //  Method Name : reconstruct_path
    //  Description : reconstruct the path from goal to start
    //  Arguments   : String name
    //  Return      : Node, if name is found
    //                null, if name is not found
    //------------------------------------------------------------------------
    private void reconstruct_path(Node path)
    {
        LinkedList<Node> nodes = new LinkedList<Node>();

        while(path != null)
        {
            nodes.addFirst(path);
            path = path.parent;
        }

        Iterator<Node> temp = nodes.iterator();

        while(temp.hasNext())
        {
            System.out.print(temp.next().name + "->");
        }
        System.out.println();
    }

    //------------------------------------------------------------------------
    //  Method Name : getLowestFscore
    //  Description : gets the lowest f(n) in a given list of nodes
    //  Arguments   : LinkedList<Node> list
    //  Return      : Node with lowest fscore
    //------------------------------------------------------------------------
    private Node getLowestFscore(LinkedList<Node> list)
    {
        Iterator<Node> iterator = list.iterator();

        iterator.hasNext();
        Node lowest = iterator.next();

        while(iterator.hasNext())
        {
            Node temp = iterator.next();
            if(lowest.f > temp.f)
            {
                lowest = temp;
            }
        }
        return lowest;
    }
}