package domain;

/* *
 *
 * @author Profesor Lic. Gilberth Chaves A.
 * BST = Binary Search Tree (Arbol de Búsqueda Binaria)
 * */
public class BST implements  Tree {
    private BTreeNode root; //se refiere a la raiz del arbol

    @Override
    public BTreeNode getRoot() {
        return root;
    }

    @Override
    public int size() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return size(root);
    }

    private int size(BTreeNode node){
        if(node==null) return 0;
        else return 1 + size(node.left) + size(node.right);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    @Override
    public boolean contains(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return binarySearch(root, element);
    }

    private boolean binarySearch(BTreeNode node, Object element){
        if(node==null) return false;
        else if(util.Utility.compare(node.data, element)==0) return true;
        else if(util.Utility.compare(element, node.data)<0)
            return binarySearch(node.left, element);
        else return binarySearch(node.right, element);
    }

    @Override
    public void add(Object element) {
       this.root = add(root, element);
    }

    private BTreeNode add(BTreeNode node, Object element){
        if(node==null)
            node = new BTreeNode(element);
        else if(util.Utility.compare(element, node.data)<0)
            node.left = add(node.left, element);
        else if(util.Utility.compare(element, node.data)>0)
            node.right = add(node.right, element);
        return node;
    }

    @Override
    public void remove(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        root = remove(root, element);
    }

    private BTreeNode remove(BTreeNode node, Object element) throws TreeException{
        if(node!=null){
            if(util.Utility.compare(element, node.data)<0)
              node.left = remove(node.left, element);
            else if(util.Utility.compare(element, node.data)>0)
                node.right = remove(node.right, element);
            else if(util.Utility.compare(node.data, element)==0){
                //caso 1. es un nodo si hijos, es una hoja
                if(node.left==null && node.right==null) return null;
                //caso 2-a. el nodo solo tien un hijo, el hijo izq
                else if (node.left!=null&&node.right==null) {
                    return node.left;
                } //caso 2-b. el nodo solo tien un hijo, el hijo der
                else if (node.left==null&&node.right!=null) {
                    return node.right;
                }
                //caso 3. el nodo tiene dos hijos
                else{
                //else if (node.left!=null&&node.right!=null) {
                    /* *
                     * El algoritmo de supresión dice que cuando el nodo a suprimir
                     * tiene 2 hijos, entonces busque una hoja del subarbol derecho
                     * y sustituya la data del nodo a suprimir por la data de esa hoja,
                     * luego elimine esa hojo
                     * */
                    Object value = min(node.right);
                    node.data = value;
                    node.right = remove(node.right, value);
                }
            }
        }
        return node; //retorna el nodo modificado o no
    }

    @Override
    public int height(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return height(root, element, 0);
    }

    //devuelve la altura de un nodo (el número de ancestros)
    private int height(BTreeNode node, Object element, int level){
        if(node==null) return 0;
        else if(util.Utility.compare(node.data, element)==0) return level;
        else return Math.max(height(node.left, element, ++level),
                    height(node.right, element, level));
    }

    @Override
    public int height() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        //return height(root, 0); //opción-1
        return height(root); //opción-2
    }

    //devuelve la altura del árbol (altura máxima de la raíz a
    //cualquier hoja del árbol)
    private int height(BTreeNode node, int level){
        if(node==null) return level-1;//se le resta 1 al nivel pq no cuente el nulo
        return Math.max(height(node.left, ++level),
                height(node.right, level));
    }

    //opcion-2
    private int height(BTreeNode node){
        if(node==null) return -1; //retorna valor negativo para eliminar el nivel del nulo
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    @Override
    public Object min() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return min(root);
    }

    private Object min(BTreeNode node){
        if(node.left!=null)
            return min(node.left);
        return node.data;
    }

    @Override
    public Object max() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return max(root);
    }

    private Object max(BTreeNode node){
        if(node.right!=null)
            return max(node.right);
        return node.data;
    }

    private void updateHeights(BTreeNode node) {
        if (node != null) {
            updateHeights(node.left);  // Recalcula altura del subárbol izquierdo
            updateHeights(node.right); // Recalcula altura del subárbol derecho
            node.height = calculateNodeHeightValue(node); // Calcula la altura para este nodo
        }
    }

    private int calculateNodeHeightValue(BTreeNode node) {
        if (node == null) {
            return -1;
        }
        int leftSubtreeHeight = (node.left != null) ? node.left.height : -1;
        int rightSubtreeHeight = (node.right != null) ? node.right.height : -1;
        return Math.max(leftSubtreeHeight, rightSubtreeHeight) + 1;
    }

    public int getElementHeight(Object element) throws TreeException {
        if (isEmpty()) throw new TreeException("BST is empty. Cannot get element height.");
        return getElementHeight(this.root, element);
    }

    private int getElementHeight(BTreeNode node, Object element) throws TreeException {
        if (node == null) {
            throw new TreeException("Element [" + element + "] does not exist in the BST.");
        }
        int result = util.Utility.compare(element, node.data);
        if (result == 0) {
            return node.height;
        } else if (result < 0) {
            return getElementHeight(node.left, element);
        } else {
            return getElementHeight(node.right, element);
        }
    }

    public String getAllElementHeightsAsString() throws TreeException {
        if (isEmpty()) throw new TreeException("BST is empty. Cannot get all element heights.");
        String result = collectElementHeightsAsString(this.root);
        // Eliminar la última ", " si existe y no está vacío
        if (result.length() > 2) { // Asegurarse de que haya al menos un "X (h:Y), " para recortar
            return result.substring(0, result.length() - 2);
        }
        return result; // Si el árbol tiene un solo nodo o está vacío (aunque el throw lo maneja)
    }

    private String collectElementHeightsAsString(BTreeNode node) {
        String result = "";
        if (node != null) {
            // Recorrido inorden para orden lógico
            result += collectElementHeightsAsString(node.left);
            result += node.data + " (h:" + node.height + "), ";
            result += collectElementHeightsAsString(node.right);
        }
        return result;
    }

    @Override
    public String preOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return preOrder(root);
    }

    //recorre el árbol de la forma: nodo-hijo izq-hijo der
    private String preOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result = node.data+" ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return  result;
    }

    @Override
    public String inOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return inOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-nodo-hijo der
    private String inOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = inOrder(node.left);
            result += node.data+" ";
            result += inOrder(node.right);
        }
        return  result;
    }

    //para mostrar todos los elementos existentes
    @Override
    public String postOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return postOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-hijo der-nodo,
    private String postOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = postOrder(node.left);
            result += postOrder(node.right);
            result += node.data+" ";
        }
        return result;
    }

    @Override
    public String toString() {
        String result="Binary Search Tree Content:";
        try {
            result = "PreOrder: "+preOrder();
            result+= "\nInOrder: "+inOrder();
            result+= "\nPostOrder: "+postOrder();

        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean isBalanced() throws TreeException {
        if (isEmpty()) {
            // Un árbol vacío generalmente se considera balanceado.
            // Podrías lanzar una excepción aquí si tu definición requiere un árbol no vacío.
            return true;
        }
        return isBalanced(root);
    }

    private boolean isBalanced(BTreeNode node) {
        // Un nodo nulo (subárbol vacío) siempre está balanceado.
        if (node == null) {
            return true;
        }

        // Obtener la altura de los subárboles izquierdo y derecho.
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);

        // Verificar si el nodo actual está balanceado.
        // La diferencia absoluta entre las alturas debe ser como máximo 1.
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return false;
        }

        // Verificar recursivamente si ambos subárboles izquierdo y derecho están balanceados.
        return isBalanced(node.left) && isBalanced(node.right);
    }

}
