 public void GreedyBestFirstSearch(string start_place, string goal_place)
        {
            LinkedList<Node> openlist = new LinkedList<Node>();
            LinkedList<Node> closedlist = new LinkedList<Node>();

            Node? start = getNodeByName(start_place);

            if(start == null)
            {
                Console.WriteLine("Place not found!");
                return;
            }
            start.f = start.h;
            openlist.AddFirst(start);

            while(openlist.Count > 0)
            {
                Node current = getlowestFscore(openlist);

                openlist.Remove(current);
                closedlist.AddLast(current);

                if (current.name == goal_place)
                {
                    reconstruct_path(current);
                    return;
                }

                LinkedList<Edge>.Enumerator iterator = current.neighbors.GetEnumerator();

                while (iterator.MoveNext())
                {
                    Node m = iterator.Current.node;

                    if (!closedlist.Contains(m))
                    {
                        m.f = m.h;
                        m.parent = current;
                        openlist.AddLast(m);
                    }
                }
            }
            Console.WriteLine("No Solution!");
        }
