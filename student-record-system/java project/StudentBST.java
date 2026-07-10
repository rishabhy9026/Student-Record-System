public class StudentBST {
    class Node {
        Student data;
        Node left, right;

        Node(Student data) {
            this.data = data;
        }
    }

    private Node root;
public void insert(Student s) {
    root = insertRec(root, s);
    System.out.println("INSERTED: " + s.toString()); // Debug line
}


    private Node insertRec(Node root, Student s) {
        if (root == null) return new Node(s);
        if (s.getRoll() < root.data.getRoll())
            root.left = insertRec(root.left, s);
        else if (s.getRoll() > root.data.getRoll())
            root.right = insertRec(root.right, s);
        return root;
    }

    public Student search(int roll) {
        Node res = searchRec(root, roll);
        return res != null ? res.data : null;
    }

    private Node searchRec(Node root, int roll) {
        if (root == null || root.data.getRoll() == roll)
            return root;
        if (roll < root.data.getRoll())
            return searchRec(root.left, roll);
        return searchRec(root.right, roll);
    }

    public void delete(int roll) {
        root = deleteRec(root, roll);
    }

    private Node deleteRec(Node root, int roll) {
        if (root == null) return null;
        if (roll < root.data.getRoll())
            root.left = deleteRec(root.left, roll);
        else if (roll > root.data.getRoll())
            root.right = deleteRec(root.right, roll);
        else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            Node min = findMin(root.right);
            root.data = min.data;
            root.right = deleteRec(root.right, min.data.getRoll());
        }
        return root;
    }

    private Node findMin(Node root) {
        while (root.left != null) root = root.left;
        return root;
    }

    public String getAllRecords() {
        StringBuilder sb = new StringBuilder();
        inorder(root, sb);
        return sb.toString().trim();
    }

    private void inorder(Node root, StringBuilder sb) {
        if (root != null) {
            inorder(root.left, sb);
            sb.append(root.data.toString()).append("\n");
            inorder(root.right, sb);
        }
    }
}
