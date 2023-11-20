import java.util.ArrayList;
import java.util.List;

abstract class BinaryTree {

    protected class Node implements TreePrinter.PrintableNode {
        protected int key;
        protected Node parent;
        protected Node left;
        protected Node right;
        public Node(int k, Node p) {
            key = k;
            parent = p;
            left = null;
            right = null;
        }
        public Node getLeft() {return left;}
        public Node getRight() {return right;}
        public int getKey() {return key;}
        public String toString() {return ""+key;}
    }

    protected Node root;
    BinaryTree() {
        root = null;
    }
}

class BinarySearchTree extends BinaryTree { //

    /* Modify the following methods */

    /* Part (a) methods */

    public void delete(int k) {
        if (root != null) {
            deleteNode(root, k);
        }
    }
    private void deleteNode(Node n, int key) {
        // delete node containing k
        // (can assume there is a node containing k)
        Node delete = getDescendant(n, key);
        if (delete.left == null && delete.right == null){ // leaf
            if (delete == root){
                root = null;
            }
            else{
                if (delete.key >= delete.parent.key){
                    delete.parent.right = null;
                }
                else{
                    delete.parent.left = null;
                }
            }
        }
        else if (delete.left == null) {// one child left
            if (delete == root){
                root.right.parent = null;
                root = root.right;
            }
            else{
                if (delete.key >= delete.parent.key){
                    delete.parent.right = delete.right;
                    delete.parent.right.parent = delete.parent;
                }
                else{
                    delete.parent.left = delete.right;
                    delete.parent.left.parent = delete.parent;
                }
            }
        }
        else if (delete.right == null) { // one child right
            if (delete == root){
                root.left.parent = null;
                root = root.left;
            }
            else{
                if (delete.key >= delete.parent.key){
                    delete.parent.right = delete.left;
                    delete.parent.right.parent = delete.parent;
                }
                else{
                    delete.parent.left = delete.left;
                    delete.parent.left.parent = delete.parent;
                }
            }
        }
        else if (delete.right  != null && delete.left != null){ // two children
            int predecessorKey = predecessorKey(key);
            Node predecessor = predecessorNode(delete);
            deleteNode(predecessor, predecessorKey);
            if (delete == root){
                root.key = predecessorKey;
            }
            else{
                if (delete.key < delete.parent.key){
                    delete.parent.left.key = predecessorKey;
                    delete.parent.left.parent = delete.parent;
                }
                else{
                    delete.parent.right.key = predecessorKey;
                    delete.parent.right.parent = delete.parent;
                }
            }
        }
    }
    public int getHeight(){
        if (root.right == null && root.left == null){
            return 0;
        }
        int height = treeHeight(root, 1);
        return height;
    }
    private int treeHeight(Node n, int height){
        if (n.left == null && n.right == null){
            return 0;
        }
        else if (n.left != null) {
            return treeHeight(n.left, height + 1);
        }
        else if (n.right != null) {
            return treeHeight(n.right, height + 1);
        }
        else if (n.left != null && n.right != null){
            if (treeHeight(n.left, height + 1) > treeHeight(n.right, height + 1)){
                return treeHeight(n.left, height + 1);
            }
            return treeHeight(n.right, height + 1);
        }
        return height;
    }
    public int successorKey(int k) {
        // return successor of node containing k
        // (can assume there is a node containing k and k is not the max)

        Node n = root;
        n = getDescendant(n, k);
        if (n.right != null){
            return minDescendant(n.right).key;
        }
        Node imSoTiredOfThis = n.parent;
        while (imSoTiredOfThis.left != n) {
            n = imSoTiredOfThis;
            imSoTiredOfThis = n.parent;
        }
        return imSoTiredOfThis.key;
    }

    /* Part (b) methods */

    public int medianKey() {
        if (root == null) {
            return 0;
        }
        return findMedianKey();
    }
    private int findMedianKey(){
        List<Integer> treeForAverage = new ArrayList<>();
        treeAsArray(root, treeForAverage);
        int key = (treeForAverage.size() + 1)/2;
        return (treeForAverage.get(key - 1));
    }
    private void treeAsArray(Node n, List tree) {
        if (n.left != null){
            treeAsArray(n.left, tree);
        }
        tree.add(n.key);
        if (n.right != null){
            treeAsArray(n.right, tree);
        }
    }

    /* Part (c) methods */

    public int nodesInLevel(int h) { //Assuming that binary tree atleast has root, may have to change
        // return the number of nodes in level h
        // (can assume h >= 0, but nothing else. The answer might be 0)
        if (h == 0) {
            if (root == null){
                return 0;
            }
            return 1;
        }
        return countNodes(h, root); // check that h > height of actual tree, if so answer 0
    }
    private int countNodes(int height, Node n){
        if (n == null){
            return 0;
        }
        else if (height == 0){
            return 1;
        }
        return countNodes(height - 1, n.left) + countNodes(height - 1, n.right);
    }
    public int minKey() {
        // return minimum stored key
        // (can assume tree is nonempty)
        return minDescendant(root).key;
    }
    private Node minDescendant(Node n) {
        // assumes n is not null
        Node current = n;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public boolean contains(int needle) {
        Node ret = getDescendant(root, needle);
        if (ret == null) {return false;}
        return true;
    }
    private Node getDescendant(Node n, int needle) {
        if (n == null) {return null;}
        if (needle == n.key) {return n;}
        else if (needle < n.key) {return getDescendant(n.left, needle);}
        else {return getDescendant(n.right, needle);}
    }

    public void add(int key) {
        if (root == null) {
            root = new Node(key, null);
        }
        else {
            addDescendant(key, root);
        }
    }
    private void addDescendant(int key, Node n) {
        // assumes n is not null
        if (key < n.key) {
            if (n.left == null) {
                n.left = new Node(key, n);
            }
            else {
                addDescendant(key, n.left);
            }
        }
        else {
            if (n.right == null) {
                n.right = new Node(key, n);
            }
            else {
                addDescendant(key, n.right);
            }
        }
    }

    public int maxKey() {
        // assumes root is not null
        return maxDescendant(root).key;
    }

    private Node maxDescendant(Node n) {
        // assumes n is not null
        Node current = n;
        while (current.right != null) {
            current = current.right;
        }
        return current;
    }

    public int predecessorKey(int key) {
        // assumes key is not the minimum
        Node n = getDescendant(root, key);
        return predecessorNode(n).key;
    }

    private Node predecessorNode(Node x) {
        if (x.left != null) {return maxDescendant(x.left);}
        Node ancestor = x.parent;
        while (x != ancestor.right) {
            x = ancestor;
            ancestor = x.parent;
        }
        return ancestor;
    }

    public void output() {
        TreePrinter.print(root);
    }

    public static void main(String[] args) {
        java.util.Scanner myScanner = new java.util.Scanner(System.in);
        BinarySearchTree myTree = new BinarySearchTree();
        boolean done = false;
        while (!done) {
            String operation = myScanner.next();
            if (operation.equals("add")) {
                myTree.add(myScanner.nextInt());
            }
            else if (operation.equals("contains")) {
                System.out.println(myTree.contains(myScanner.nextInt()));
            }
            else if (operation.equals("maxKey")) {
                System.out.println(myTree.maxKey());
            }
            else if (operation.equals("output")) {
                myTree.output();
            }
            else if (operation.equals("predecessorKey")) {
                System.out.println(myTree.predecessorKey(myScanner.nextInt()));
            }
            else if (operation.equals("height")) {
                System.out.println(myTree.getHeight());
            }
            else if (operation.equals("successorKey")) {
                System.out.println(myTree.successorKey(myScanner.nextInt()));
            }
            else if (operation.equals("minKey")) {
                System.out.println(myTree.minKey());
            }
            else if (operation.equals("medianKey")) {
                System.out.println(myTree.medianKey());
            }
            else if (operation.equals("delete")) {
                myTree.delete(myScanner.nextInt());
            }
            else if (operation.equals("nodesInLevel")) {
                System.out.println(myTree.nodesInLevel(myScanner.nextInt()));
            }
            else if (operation.equals("quit")) {
                done = true;
            }
        }
    }
}


/**
 * Binary Tree Printer
 * @author MightyPork
 * @see <a href="https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram-in-java">source</a>
 */
class TreePrinter
{
    /** Node that can be printed */
    public interface PrintableNode {
        /** Get left child */
        PrintableNode getLeft();

        /** Get right child */
        PrintableNode getRight();
    }

    /**
     * Print a tree
     */
    public static void print(PrintableNode root) {
        List<List<String>> lines = new ArrayList<List<String>>();

        List<PrintableNode> level = new ArrayList<PrintableNode>();
        List<PrintableNode> next = new ArrayList<PrintableNode>();

        level.add(root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<String>();

            nn = 0;

            for (PrintableNode n : level) {
                if (n == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                } else {
                    String aa = n.toString();
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.getLeft());
                    next.add(n.getRight());

                    if (n.getLeft() != null) nn++;
                    if (n.getRight() != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;

            lines.add(line);

            List<PrintableNode> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {

                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            // print line of numbers
            for (int j = 0; j < line.size(); j++) {

                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                // a number
                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            perpiece /= 2;
        }
    }
}
