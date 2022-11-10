//------------------------------------------------------------------------
// 2022 IT-ELAI Introduction to AI
// Topic : Informed Search Algorithms
//------------------------------------------------------------------------
//
// File Name    :   GraphTraversal.cs
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
// $000 -------  0.1  001 2022-11-5 cabrillosa  First Release.
using System;
namespace BestFirstSearch
{
    public class GraphTraversal
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
        public void add_place(string place, int h)
        {
            Node n = new Node();
            n.name = place;
            n.h = h;
            graph.AddLast(n);

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
        public int connect(string place1, string place2, int dist)
        {
            Node? n1 = getNodeByName(place1);
            Node? n2 = getNodeByName(place2);

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
            LinkedList<Node>.Enumerator iterator = graph.GetEnumerator();

            while (iterator.MoveNext())
            {
                Console.Write(iterator.Current.name +"::");
                LinkedList<Edge>.Enumerator edges = iterator.Current.neighbors.GetEnumerator();

                while(edges.MoveNext())
                {
                    Console.Write(edges.Current.node.name + " -> ");
                }
                Console.WriteLine(); 
            }
        }

        //------------------------------------------------------------------------
        //  Method Name : GreedyBestFirstSearch
        //  Description : Traverse the map using Greedy Best First Search
        //  Arguments   : string start_place
        //                string goal_place
        //  Return      : Void
        //------------------------------------------------------------------------
        public void GreedyBestFirstSearch(string start_place, string goal_place)
        {
            Node? start = getNodeByName(start_place);
            PriorityQueue<Node, int> pq = new PriorityQueue<Node, int>();

            if(start == null)
            {
                Console.WriteLine("Node not found!");
                return;
            }

            start.f = start.h;
            start.isVisited = true;
            pq.Enqueue(start, start.f);


            while(pq.Count > 0)
            {
                Node current = pq.Dequeue();
                current.isVisited = true;
                if(current.name == goal_place)
                {
                    //we reached the goal
                    reconstruct_path(current);
                    return;
                }

                LinkedList<Edge>.Enumerator iterator = current.neighbors.GetEnumerator();
                while(iterator.MoveNext())
                {
                    if(iterator.Current.node.isVisited != true)
                    {
                        Node temp = iterator.Current.node;
                        temp.f = temp.h;
                        temp.parent = current;
                        pq.Enqueue(iterator.Current.node, temp.f);
                    }    
                }
            }
            Console.WriteLine("No path to goal!");
        }

        //------------------------------------------------------------------------
        //  Method Name : astar
        //  Description : Traverse the map using A*
        //  Arguments   : string start_place
        //                string goal_place
        //  Return      : Void
        //------------------------------------------------------------------------
        public void astar(string start_place, string goal_place)
        {
            Node ?start = getNodeByName(start_place);

            if (start == null)
            {
                Console.WriteLine("Node not found!");
                return;
            }

            LinkedList<Node> openlist = new LinkedList<Node>();
            LinkedList<Node> closedlist = new LinkedList<Node>();

            start.f = start.g + start.h;
            openlist.AddLast(start);

            while(openlist.Count > 0)
            {
                Node current = getLowestFscore(openlist);

                if(current.name == goal_place)
                {
                    Console.WriteLine(current.f + " is the final F score!");
                    reconstruct_path(current);
                    return;
                }

                LinkedList<Edge>.Enumerator neighbor = current.neighbors.GetEnumerator();

                while(neighbor.MoveNext())
                {
                    Edge m = neighbor.Current;
                    int g_so_far = current.g + m.weight;

                    if(!closedlist.Contains(m.node) && !openlist.Contains(m.node))
                    {
                        m.node.parent = current;
                        m.node.g = g_so_far;
                        m.node.f = m.node.g + m.node.h;
                        openlist.AddLast(m.node);
                    }
                    else
                    {
                        if(g_so_far < m.node.g)
                        {
                            m.node.parent = current;
                            m.node.g = g_so_far;
                            m.node.f = m.node.g + m.node.h;

                            if (closedlist.Contains(m.node))
                            {
                                openlist.AddLast(m.node);
                            }
                        }
                    }
                }
                openlist.Remove(current);
                closedlist.AddLast(current);
            }
            Console.WriteLine("No path to goal!");
        }

        // ----------------------
        //  UTILITY FUNCTIONS
        //-----------------------

        //------------------------------------------------------------------------
        //  Method Name : getNodeByName
        //  Description : get the node by its name
        //  Arguments   : string name
        //  Return      : Node, if name is found
        //                null, if name is not found
        //------------------------------------------------------------------------
        private Node? getNodeByName(string name)
        {
            LinkedList<Node>.Enumerator iterator = graph.GetEnumerator();

            while(iterator.MoveNext()) //n = n.next
            {
                Node n = iterator.Current;

                if(n.name == name)
                {
                    //string is in the graph, return!
                    return n;
                }
            }

            return null;
        }

        //------------------------------------------------------------------------
        //  Method Name : reconstruct_path
        //  Description : reconstruct the path from goal to start
        //  Arguments   : string name
        //  Return      : Node, if name is found
        //                null, if name is not found
        //------------------------------------------------------------------------
        private void reconstruct_path(Node? path)
        {
            LinkedList<Node> nodes = new LinkedList<Node>();

            while(path != null)
            {
                nodes.AddFirst(path);
                path = path.parent;
            }

            LinkedList<Node>.Enumerator temp = nodes.GetEnumerator();

            while(temp.MoveNext())
            {
                Console.Write(temp.Current.name + "->");
            }
            Console.WriteLine();
        }

        //------------------------------------------------------------------------
        //  Method Name : getLowestFscore
        //  Description : gets the lowest f(n) in a given list of nodes
        //  Arguments   : LinkedList<Node> list
        //  Return      : Node with lowest fscore
        //------------------------------------------------------------------------
        private Node getLowestFscore(LinkedList<Node> list)
        {
            LinkedList<Node>.Enumerator iterator = list.GetEnumerator();

            iterator.MoveNext();
            Node lowest = iterator.Current;

            while(iterator.MoveNext())
            {
                if(lowest.f > iterator.Current.f)
                {
                    lowest = iterator.Current;
                }
            }
            return lowest;
        }
    }
}
//end of file

