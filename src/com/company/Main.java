package com.company;

import java.io.*;
import java.util.*;

/**
 * Written by Renata Shakirova, B20-03
 */

public class Main {
    /**
     * Class Vertex to store vertices of the graph.
     * @param <T> is the type of the value of the vertex.
     */
    static class Vertex<T>{
        T value;
        int id;
        int color;

        Vertex(T val, int i){
            this.value = val;
            this.id = i;
            color = 0;
        }

        public void setColor(int col){
            this.color = col;
        }
    }

    /**
     * Class Edge to store the edges of the graph. It stores two vertices and the weight.
     * @param <E> The type of the value of yhe  weight.
     * @param <T> The type of the value of the vertice.
     */
    static class Edge<E, T>{
        Vertex<T> from, to;
        E weight;

        Edge(Vertex<T> from, Vertex<T> to, E weight){
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    /**
     * Class Weight and Bandwidth, serves to store both the weight of the edge and the bandwidth.
     *
     * Used in taskB to solve it more conveniently.
     */
    static class W_and_B{
        int weight, bandwidth;
        W_and_B(int weigth, int bandwidth){
            this.weight = weigth;
            this.bandwidth = bandwidth;
        }
    }

    /**
     * The interface used to implement the graph.
     * @param <T> The type of the value of the vertex.
     * @param <E> The type of the weight of the edge.
     */
    interface Graph<T, E> {
        /**
         * Adds vertex with the specified value.
         * @param value The value of the vertex
         * @return The reference to the vertex created.
         */
        Vertex<T> addVertex(T value);

        /**
         * Removes the given vertex from the graph.
         * @param v The given vertex.
         */
        void removeVertex(Vertex<T>  v) throws Exception;

        /**
         * Adds a new directed edge between the vertices.
         * @param from The vertex from which the edge begins
         * @param to The vertex at which the edge ends.
         * @param weight The weight of the edge.
         * @return A reference to the edge created.
         */
        Edge<E, T> addEdge(Vertex<T>  from, Vertex<T>  to, E weight) throws Exception;

        /**
         * Removes the specified edge.
         * @param e The specified edge.
         */
        void removeEdge(Edge<E, T> e) throws Exception;

        /**
         * Returns all the edges from the specified vertex v
         * @param v The specified vertex v
         * @return ArrayList of the requested edges
         */
        ArrayList<Edge<E, T>> edgesFrom(Vertex<T> v) throws Exception;

        /**
         * Returns all the edges to the specified vertex v
         * @param v The specified vertex v
         * @return ArrayList of the requested edges
         */
        ArrayList<Edge<E, T>> edgesTo(Vertex<T> v) throws Exception;

        /**
         * Finds the vertex of the specified value.
         * @param value The specified value of the vertex
         * @return The found vertex
         */
        Vertex<T> findVertex(T value);

        /**
         * Finds edge from one vertex with specified value 1 to the other vertex with specified value 2
         * @param from_value The value of the vertex from which the egde begins.
         * @param to_value The value of the vertex at which the edge ends
         * @return The found edge
         */
        Edge<E, T> findEdge(T from_value, T to_value);

        /**
         * Checks if there exists an edge from one specified vertex to the other specified vertex.
         * @param v The vertex from which the edge begins.
         * @param u The vertex at which the edge ends.
         * @return bool if the edge exists
         */
        boolean hasEdge(Vertex<T> v, Vertex<T> u) throws Exception;

    }

    /**
     * The implementation of the graph interface using adjacency matrix.
     * @param <T> The type of the value of the vertex.
     * @param <E> The type of the weight of the edge.
     */
    static class AdjacencyMatrixGraph<T, E> implements Graph<T, E>{

        HashMap<T, Integer> id; //Hashmapp to get the index of the vertex by its value
        ArrayList<Vertex<T>> val; //The arrays of the vertices
        ArrayList<ArrayList<E>> adjacencyMatrix; //The adjacency matrix which stores the edges.

        /**
         * Initializes all the structures.
         */
        public AdjacencyMatrixGraph() {
            id = new HashMap<>();
            val = new ArrayList<>();
            adjacencyMatrix = new ArrayList<>();
        }

        /**
         * Adds the vertex.
         * Creates the vertex by giving it an id and a specified value.
         * Adds the vertex to the adjacency matrix and adds the value of the vertex to the map, mapping it with its index.
         * Adds the vertex to the list of vertices.
         * @param value The value of the vertex
         * @return The created vertex
         */
        @Override
        public Vertex<T> addVertex(T value) {
            if(id.containsKey(value)){
                return val.get(id.get(value));
            }
            ArrayList<E> newArr = new ArrayList<>();
            for(int i = 0; i < id.size(); i++){
                newArr.add(null);
            }
            adjacencyMatrix.add(newArr);
            Vertex<T> v = new Vertex<>(value, id.size());
            id.put(value, id.size());
            val.add(v);
            for(ArrayList<E> arr: adjacencyMatrix){
                arr.add(null);
            }
            return v;
        }

        /***
         * Removes the given vertex from the graph.
         * Removes it from arrays of adjacency matrix, removes its value from map, and removes it from the list of vertices.
         * After that renews the indices of the vertices: all the vertices with the index greated than deleted one get their index reduced by one.
         * @param v The given vertex.
         */
        @Override
        public void removeVertex(Vertex<T> v) throws Exception {
            if(v == null) throw new Exception("Vertix is null!");
            int index = v.id;
            id.remove(v.value);
            val.remove(index);
            for(int i = 0; i < id.size(); i++){
                if(val.get(i).id > i){
                    val.get(i).id = i;
                }
            }
            for(T val : id.keySet()){
                if(id.get(val) > index){
                    id.put(val, id.get(val) - 1);
                }
            }
            adjacencyMatrix.remove(index);
            for(ArrayList<E> arr : adjacencyMatrix){
                arr.remove(index);
            }
        }

        /**
         * Adds a new edge to the graph. Adds its weight to the adjacency matrix.
         * Then creates the edge.
         * @param from The vertex from which the edge begins
         * @param to The vertex at which the edge ends.
         * @param weight The weight of the edge.
         * @return The created edge.
         */
        @Override
        public Edge<E, T> addEdge(Vertex<T> from, Vertex<T> to, E weight) throws Exception {
            if(from == null || to == null) throw new Exception("The vertex is null!");
            int id1 = from.id;
            int id2 = to.id;
            adjacencyMatrix.get(id1).set(id2, weight);
            return new Edge<>(from, to, weight);
        }

        /**
         * Removes the Specified edge from the graph. Puts null to the weight of the edge at the adjacency matrix.
         * @param e The specified edge.
         */
        @Override
        public void removeEdge(Edge<E, T> e) throws Exception {
            if(e == null) throw new Exception("The edge is null!");
            int id1 = e.from.id;
            int id2 = e.to.id;
            adjacencyMatrix.get(id1).set(id2, null);
        }

        /**
         * Gets all the edges from the specified vertex by running through adjacency matrix and finding non-null spots.
         * The method then creates the edges and adds them to the array, which is returned as the answer.
         * @param v The specified vertex v
         * @return ArrayList of the requested edges.
         */
        @Override
        public ArrayList<Edge<E, T>> edgesFrom(Vertex<T> v) throws Exception {
            if(v == null) throw new Exception("The vertex is null!");
            int curId = v.id;
            ArrayList<Edge<E, T>> answer = new ArrayList<>();
            for(int i = 0; i < id.size(); i++){
                E w = adjacencyMatrix.get(curId).get(i);
                if(w != null){
                    answer.add(new Edge<>(v, val.get(i), w));
                }
            }
            return answer;
        }

        /*** Gets all the edges to the specified vertex by running through adjacency matrix and finding non-null spots.
         * The method then creates the edges and adds them to the array, which is returned as the answer.
         * @param v The specified vertex v
         * @return ArrayList of the requested edges.
         */
        @Override
        public ArrayList<Edge<E, T>> edgesTo(Vertex<T> v) throws Exception {
            if(v == null) throw new Exception("The vertex is null!");
            int curId = id.get(v.value);
            ArrayList<Edge<E, T>> answer = new ArrayList<>();
            for(int i = 0; i < id.size(); i++){
                E w = adjacencyMatrix.get(i).get(curId);
                if(w != null){
                    answer.add(new Edge<>(val.get(i), v, w));
                }
            }
            return answer;
        }

        /**
         * Finds the vertex by the specified value.
         * Checks the map: if the value exists, then such vertex exists and is returned.
         * If the value is not the key of the map, then there is no such vertex - null is returned.
         * @param value The specified value of the vertex
         * @return The found vertex or null.
         */
        @Override
        public Vertex<T> findVertex(T value) {
            if(id.containsKey(value)){
                return new Vertex<>(value, id.get(value));
            } else {
                return null;
            }
        }

        /**
         * Finds the edge between the vertices of the specified valyes. \
         * Gets the ids of the vertices by accessing the map by specified values and checks if the weight is not null in the adjacecny matrix.
         * If the weight is null, then there is no such edge and null is returned.
         * @param from_value The value of the vertex from which the egde begins.
         * @param to_value The value of the vertex at which the edge ends
         * @return The found edge or null.
         */
        @Override
        public Edge<E, T> findEdge(T from_value, T to_value) {
            if(id.containsKey(from_value) && id.containsKey(to_value)) {
                int id1 = id.get(from_value);
                int id2 = id.get(to_value);
                Vertex<T> from = new Vertex<>(from_value, id1);
                Vertex<T> to = new Vertex<>(to_value, id2);
                E w = adjacencyMatrix.get(id1).get(id2);
                if (w != null) {
                    return new Edge<>(from, to, w);
                }
            }
            return null;
        }

        /**
         * Checks if there is an edge between the two vertices.
         * @param v The vertex from which the edge begins.
         * @param u The vertex at which the edge ends.
         * @return bool if the edge exists or not
         */
        @Override
        public boolean hasEdge(Vertex<T> v, Vertex<T> u) throws Exception {
            if(v == null || u == null) throw new Exception("Some vertices are null");
            int id1 = v.id;
            int id2 = u.id;
            E w = adjacencyMatrix.get(id1).get(id2);
            return w != null;
        }

        /**
         * Rerturns arrayList of the vertices which complete the cycle, or null is there is no cycle.
         * Tp find a cycle, it starts the dfs from any unchecked vertex.
         * @return The array of the vertices which make up a cycle or null.
         */
        ArrayList<Vertex<T>> isAcyclic(){
            for(int i = 0; i < id.size(); i++){
                val.get(i).setColor(0);
            }
            for(int i = 0; i < id.size(); i++) {
                if(val.get(i).color == 0) {
                    ArrayList<Vertex<T>> answer = getCycle(val.get(i), new ArrayList<>());
                    if(answer != null) return answer;
                }
            }
            return null;
        }

        /**
         * The DFS looking for the cycle, if the vertex of the color 1 leads to the vertex of the color 1 as well, the cycle is found.
         * The DFS contains the current path as well, and if it finds the cycle, it returns the part of the path which contains the cycle/
         * @param vertex The vertex being checked.
         * @param currentPath The current path of vertices.
         * @return The found cycle or null if it does not exist.
         */
        ArrayList<Vertex<T>> getCycle(Vertex<T> vertex, ArrayList<Vertex<T>> currentPath){
            currentPath.add(vertex);
            int index = vertex.id;
            vertex.setColor(1);
            for (int i = 0; i < id.size(); i++){
                if(adjacencyMatrix.get(index).get(i) != null){
                    Vertex<T> currentVertex = val.get(i);
                    if(currentVertex.color == 1){
                        ArrayList<Vertex<T>> answer = new ArrayList<>();
                        boolean metCurrent = false;
                        for(Vertex<T> cur : currentPath){
                            if(cur.id == currentVertex.id){
                                metCurrent = true;
                            }
                            if(metCurrent){
                                answer.add(cur);
                            }
                        }
                        return answer;
                    } else if(currentVertex.color == 0){
                        ArrayList<Vertex<T>> answer = getCycle(currentVertex, currentPath);
                        if (answer != null) return answer;
                    }
                }
            }
            vertex.setColor(2);
            currentPath.remove(currentPath.size() - 1);
            return null;
        }

        /**
         * The void which transposes the graph by swapping the edges in the adjacency matrix.
         */
        void transpose(){
            for(int i = 0; i < id.size(); i++){
                for(int j = 0; j < i; j++){
                    E w1 = adjacencyMatrix.get(i).get(j);
                    adjacencyMatrix.get(i).set(j, adjacencyMatrix.get(j).get(i));
                    adjacencyMatrix.get(j).set(i, w1);
                }
            }
        }
    }




    FastScanner in;
    PrintWriter out;

    /**
     * Checks all the methods of graph interface by answering requests in the file.
     * @throws IOException
     */
    private void solveA() throws Exception {
        AdjacencyMatrixGraph<String, Integer> graph = new AdjacencyMatrixGraph<>();
        while(in.hasNext()){
            String instruction = in.next();
            switch (instruction){
                case ("ADD_VERTEX"):
                    graph.addVertex(in.next());
                    break;
                case ("REMOVE_VERTEX"):
                    graph.removeVertex(graph.findVertex(in.next()));
                    break;
                case ("ADD_EDGE"):
                    String name1 = in.next(), name2 = in.next();
                    graph.addEdge(graph.findVertex(name1), graph.findVertex(name2), in.nextInt());
                    break;
                case ("REMOVE_EDGE"):
                    Edge<Integer, String> edge = graph.findEdge(in.next(), in.next());
                    graph.removeEdge(edge);
                    break;
                case ("HAS_EDGE"):
                    if(graph.hasEdge(graph.findVertex(in.next()), graph.findVertex(in.next()))){
                        out.println("TRUE");
                    } else {
                        out.println("FALSE");
                    }
                    break;
                case ("IS_ACYCLIC"):
                    ArrayList<Vertex<String>> answer = graph.isAcyclic();
                    if(answer == null) {
                        out.println("ACYCLIC");
                    } else {
                        int weight = 0;
                        int n = answer.size();
                        for(int i = 0; i < n; i++){
                            weight += graph.findEdge(answer.get(i).value, answer.get((i + 1)%n).value).weight;
                        }
                        out.print(weight + " ");
                        for (Vertex<String> stringVertex : answer) {
                            out.print(stringVertex.value + " ");
                        }
                        out.println();
                    }
                    break;
                case ("TRANSPOSE"):
                    graph.transpose();
                    break;
            }
        }

    }

    /**
     * Implements Dijkstra's algorithm on the AdjacencyMatrixGraph.
     * The algorithm is expanded in order to satisfy the conditions of the minimum bandwidth for the shortest path, which is stored in pair with the weight of the edge as the edge's total weight value.
     * All the edges with the bandwidth lesser than the required minimum one are not taken into consideration while computing the algorithm.
     * @throws IOException
     */
    private void solveB() throws Exception {
        int n = in.nextInt(), m = in.nextInt();
        AdjacencyMatrixGraph<Integer, W_and_B> graph = new AdjacencyMatrixGraph<>();
        for (int i = 0; i < n; i++) {
            graph.addVertex(i);
        }
        for(int i = 0; i < m; i++){
            int v = in.nextInt() - 1, u = in.nextInt() - 1;
            int weight = in.nextInt(), bandwidth = in.nextInt();
            graph.addEdge(graph.findVertex(v), graph.findVertex(u), new W_and_B(weight, bandwidth));
        }
        int startVertex = in.nextInt() - 1, finishVertex = in.nextInt() - 1, minBandwidth = in.nextInt();
        int[] pathValues = new int[n];
        int[] minBandWidthValues = new int[n];
        int[] prevVertices = new int[n];
        boolean[] isRelaxed = new boolean[n];
        for(int i = 0; i < n; i++){
            pathValues[i] = (int) 1e9;
            minBandWidthValues[i] = (int) 1e9;
            prevVertices[i] = -1;
        }
        pathValues[startVertex] = 0;
        boolean didRelax = true;
        for(int i = 0; i < n; i++){
            int minId = -1;
            for(int j = 0; j < n; j++) {
                if (!isRelaxed[j] && (minId == -1 || pathValues[j] < pathValues[minId])) {
                    minId = j;
                }
            }
            Vertex<Integer> currentVertex = graph.findVertex(minId);
            for(Edge<W_and_B, Integer> currentEdge : graph.edgesFrom(currentVertex)){
                if(currentEdge.weight.bandwidth >= minBandwidth && pathValues[minId] + currentEdge.weight.weight < pathValues[currentEdge.to.id]){
                    pathValues[currentEdge.to.id] = pathValues[minId] + currentEdge.weight.weight;
                    minBandWidthValues[currentEdge.to.id] = Math.min(minBandWidthValues[minId], currentEdge.weight.bandwidth);
                    prevVertices[currentEdge.to.id] = minId;
                }
            }
            isRelaxed[minId] = true;
        }
        if(pathValues[finishVertex] == (int)1e9){
            out.print("IMPOSSIBLE");
        } else {
            ArrayList<Integer> path = new ArrayList<>();
            int curVertex = finishVertex;
            path.add(curVertex + 1);
            while (curVertex != startVertex){
                curVertex = prevVertices[curVertex];
                path.add(curVertex + 1);
            }
            out.println(path.size() + " " + pathValues[finishVertex] + " " + minBandWidthValues[finishVertex]);
            for(int i = path.size() - 1; i >= 0; i--){
                out.print(path.get(i) + " ");
            }
        }
    }

    static class FastScanner {
        StringTokenizer st;
        BufferedReader br;
        FastScanner(InputStream s) {
            br = new BufferedReader(new InputStreamReader(s));
        }
        String next() throws IOException {
            while (st == null || !st.hasMoreTokens())
                st = new StringTokenizer(br.readLine());
            return st.nextToken();
        }

        String thisLine() throws IOException{
            while (st == null || !st.hasMoreTokens())
                st = new StringTokenizer(br.readLine());
            StringBuilder ans = new StringBuilder();
            while (st.hasMoreTokens()){
                ans.append(st.nextToken());
                ans.append(" ");
            }
            ans.deleteCharAt(ans.length() - 1);
            return ans.toString();
        }

        boolean hasNext() throws IOException {
            return br.ready() || (st != null && st.hasMoreTokens());
        }
        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
        long nextLong() throws IOException {
            return Long.parseLong(next());
        }
        double nextDouble() throws IOException {
            return Double.parseDouble(next());
        }

        double nextDollarDouble() throws IOException {
            return Double.parseDouble(next().substring(1));
        }

        String nextLine() throws IOException {
            return br.readLine();
        }
        boolean hasNextLine() throws IOException {
            return br.ready();
        }
    }

    private void run() throws Exception {
        //in = new FastScanner(new FileInputStream("input.txt"));
        in = new FastScanner(System.in);
        out = new PrintWriter(System.out);

        try {
            solveA();
        } catch (Exception e){
            out.println(e.getMessage());
        }
        out.flush();
        out.close();
    }
    public static void main(String[] args) throws Exception {
        new Main().run();
    }
}
