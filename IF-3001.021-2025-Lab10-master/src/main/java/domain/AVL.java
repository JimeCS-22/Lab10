package domain;

/* *
 *
 * @author Profesor Lic. Gilberth Chaves A.
 * Binary Search Tree AVL (Arbol de Búsqueda Binaria AVL)
 * AVL = Arbol de busqueda binaria auto balanceado
 * */
public class AVL implements Tree {
    private BTreeNode root; //se refiere a la raiz del arbol

    @Override
    public int size() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return size(root);
    }

    private int size(BTreeNode node) {
        if (node == null) return 0;
        else return 1 + size(node.left) + size(node.right);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return binarySearch(root, element);
    }

    private boolean binarySearch(BTreeNode node, Object element) {
        if (node == null) return false;
        else if (util.Utility.compare(node.data, element) == 0) return true;
        else if (util.Utility.compare(element, node.data) < 0)
            return binarySearch(node.left, element);
        else return binarySearch(node.right, element);
    }

    @Override
    public void add(Object element) {
        this.root = add(root, element, "root");
    }

    private BTreeNode add(BTreeNode node, Object element, String path) {
        if (node == null)
            return new BTreeNode(element, path);
        else if (util.Utility.compare(element, node.data) < 0)
            node.left = add(node.left, element, path + "/left");
        else if (util.Utility.compare(element, node.data) > 0)
            node.right = add(node.right, element, path + "/right");

        //una vez agregado el nuevo nodo, debemos determinar si se requiere rebalanceo para siga siendo BST-AVL
        return reBalance(node, element);
    }

    private BTreeNode reBalance(BTreeNode node, Object element) {
        int balance = getBalanceFactor(node);

        // Caso-1. Left Left Case
        if (balance > 1 && util.Utility.compare(element, node.left.data) < 0) {
            node.path += "/Simple-Right-Rotate";
            return rightRotate(node);
        }

        // Caso-2. Right Right Case
        if (balance < -1 && util.Utility.compare(element, node.right.data) > 0) {
            node.path += "/Simple-Left-Rotate";
            return leftRotate(node);
        }

        // Caso-3. Left Right Case
        if (balance > 1 && util.Utility.compare(element, node.left.data) > 0) {
            node.path += "/Double-Left-Right-Rotate";
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Caso-4. Right Left Case
        if (balance < -1 && util.Utility.compare(element, node.right.data) < 0) {
            node.path += "/Double-Right-Left-Rotate";
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    private int getHeight(BTreeNode node) {
        if (node == null) return -1;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    private int getBalanceFactor(BTreeNode node) {
        if (node == null) return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    private BTreeNode leftRotate(BTreeNode node) {
        BTreeNode node1 = node.right;
        if (node1 != null) {
            BTreeNode node2 = node1.left;
            node1.left = node;
            node.right = node2;
        }
        return node1;
    }

    private BTreeNode rightRotate(BTreeNode node) {
        BTreeNode node1 = node.left;
        if (node1 != null) {
            BTreeNode node2 = node1.right;
            node1.right = node;
            node.left = node2;
        }
        return node1;
    }

    @Override
    public void remove(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        root = remove(root, element);
    }

    private BTreeNode remove(BTreeNode node, Object element) throws TreeException {
        if (node == null) return null;

        if (util.Utility.compare(element, node.data) < 0) {
            node.left = remove(node.left, element);
        } else if (util.Utility.compare(element, node.data) > 0) {
            node.right = remove(node.right, element);
        } else {
            // Node found
            if (node.left == null && node.right == null) {
                return null;
            } else if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                Object successorValue = min(node.right);
                node.data = successorValue;
                node.right = remove(node.right, successorValue);
            }
        }
        return reBalanceAfterRemoval(node);
    }

    private BTreeNode reBalanceAfterRemoval(BTreeNode node) {
        if (node == null) return null;

        int balance = getBalanceFactor(node);

        // Left Heavy
        if (balance > 1) {
            if (getBalanceFactor(node.left) >= 0) {
                return rightRotate(node); // Left Left Case
            } else {
                node.left = leftRotate(node.left); // Left Right Case
                return rightRotate(node);
            }
        }
        // Right Heavy
        if (balance < -1) {
            if (getBalanceFactor(node.right) <= 0) {
                return leftRotate(node); // Right Right Case
            } else {
                node.right = rightRotate(node.right); // Right Left Case
                return leftRotate(node);
            }
        }
        return node;
    }

    @Override
    public int height(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return height(root, element, 0);
    }

    private int height(BTreeNode node, Object element, int level) {
        if (node == null) return 0;
        else if (util.Utility.compare(node.data, element) == 0) return level;
        else {
            int leftHeight = height(node.left, element, level + 1);
            if (leftHeight > 0) return leftHeight;
            return height(node.right, element, level + 1);
        }
    }

    @Override
    public int height() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return getHeight(root);
    }

    private int height(BTreeNode node) {
        return getHeight(node);
    }

    @Override
    public Object min() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return min(root);
    }

    private Object min(BTreeNode node) {
        if (node.left != null)
            return min(node.left);
        return node.data;
    }

    @Override
    public Object max() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return max(root);
    }

    private Object max(BTreeNode node) {
        if (node.right != null)
            return max(node.right);
        return node.data;
    }

    @Override
    public String preOrder() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return preOrder(root);
    }

    private String preOrder(BTreeNode node) {
        String result = "";
        if (node != null) {
            result = node.data + " ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return result;
    }

    private String preOrderPath(BTreeNode node) {
        String result = "";
        if (node != null) {
            result = node.data + "(" + node.path + ")" + " ";
            result += preOrderPath(node.left);
            result += preOrderPath(node.right);
        }
        return result;
    }

    @Override
    public String inOrder() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return inOrder(root);
    }

    String inOrder(BTreeNode node) {
        String result = "";
        if (node != null) {
            result = inOrder(node.left);
            result += node.data + " ";
            result += inOrder(node.right);
        }
        return result;
    }

    @Override
    public String postOrder() throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return postOrder(root);
    }

    private String postOrder(BTreeNode node) {
        String result = "";
        if (node != null) {
            result = postOrder(node.left);
            result += postOrder(node.right);
            result += node.data + " ";
        }
        return result;
    }

    public Object father(Object element) throws TreeException {
        if (isEmpty() || root.data.equals(element)) {
            return null;
        }
        return father(root, element);
    }

    private Object father(BTreeNode node, Object element) {
        if (node == null || node.data.equals(element)) {
            return null;
        }
        if ((node.left != null && node.left.data.equals(element)) || (node.right != null && node.right.data.equals(element))) {
            return node.data;
        } else if (util.Utility.compare(element, node.data) < 0) {
            return father(node.left, element);
        } else {
            return father(node.right, element);
        }
    }

    public Object brother(Object element) throws TreeException {
        if (isEmpty() || root.data.equals(element)) {
            return null;
        }
        return brother(root, element);
    }

    private Object brother(BTreeNode node, Object element) {
        if (node == null || node.data.equals(element)) {
            return null;
        }
        if (node.left != null && node.left.data.equals(element)) {
            if (node.right != null) {
                return node.right.data;
            }
        } else if (node.right != null && node.right.data.equals(element)) {
            if (node.left != null) {
                return node.left.data;
            }
        } else if (util.Utility.compare(element, node.data) < 0) {
            return brother(node.left, element);
        } else {
            return brother(node.right, element);
        }
        return null;
    }

    public String children(Object element) throws TreeException {
        if (isEmpty()) {
            return "";
        }
        return children(root, element);
    }

    private String children(BTreeNode node, Object element) {
        if (node == null || !node.data.equals(element)) {
            if (node != null) {
                if (util.Utility.compare(element, node.data) < 0) {
                    return children(node.left, element);
                } else {
                    return children(node.right, element);
                }
            }
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            if (node.left != null) {
                sb.append("Izquierdo: ").append(node.left.data);
            }
            if (node.right != null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append("Derecho: ").append(node.right.data);
            }
            return sb.toString();
        }
    }

    @Override
    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(BTreeNode node) {
        if (node == null) {
            return true;
        }
        int balanceFactor = getBalanceFactor(node);
        return Math.abs(balanceFactor) <= 1 && isBalanced(node.left) && isBalanced(node.right);
    }

    @Override
    public String toString() {
        String result = "AVL Binary Search Tree Content:";
        try {
            result = "PreOrder: " + preOrderPath(root);
            result += "\nPreOrder: " + preOrder();
            result += "\nInOrder: " + inOrder();
            result += "\nPostOrder: " + postOrder();
            result += "\nHeight: " + height();
            result += "\nIs Balanced: " + isBalanced();
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public BTreeNode getRoot() {
        return root;
    }

    public void setRoot(BTreeNode root) {
        this.root = root;
    }
}