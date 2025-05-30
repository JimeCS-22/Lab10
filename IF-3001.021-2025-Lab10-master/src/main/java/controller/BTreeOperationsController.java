package controller;

import domain.AVL;
import domain.BTree;
import domain.BTreeNode;
import domain.TreeException;
import domain.Tree; // Import the Tree interface
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import util.FXUtility;
import util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BTreeOperationsController {

    private Tree activeTree; // Referencia a la instancia del árbol activo
    private BTree bTree;
    private AVL avlTree;
    private static final double NODE_RADIUS = 10;
    private static final double LEVEL_SPACING = 100;
    private static final double HORIZONTAL_SPACING = 200;
    private List<Integer> treeValues = new ArrayList<>(); // Para almacenar los valores del árbol

    @FXML
    private Pane mainPain;
    private double scaleFactor = 1.0;
    @FXML
    private Pane pane2;
    @FXML
    private RadioButton bstRadioButton;
    @FXML
    private RadioButton avlRadioButton;
    @FXML
    private Label balanceLabel;
    @FXML
    private ToggleGroup treeTypeToggleGroup;

    public void initialize() {
        this.bTree = new BTree();
        this.avlTree = new AVL();
        this.activeTree = bTree; // Inicialmente el árbol activo es BST
        bstRadioButton.setSelected(true); // Seleccionar BST por defecto
        generateRandomTreeValues();
        addValuesToActiveTree();
        drawTree();
        updateBalanceLabel();

        // Listener para cambiar el tipo de árbol
        treeTypeToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == bstRadioButton) {
                switchToBST();
            } else if (newValue == avlRadioButton) {
                switchToAVL();
            }
            drawTree();
            updateBalanceLabel();
        });
    }

    private void generateRandomTreeValues() {
        treeValues.clear();
        for (int i = 0; i < 30; i++) {
            treeValues.add(Utility.random(51));
        }
    }

    private void addValuesToActiveTree() {
        activeTree.clear();
        for (int value : treeValues) {
            activeTree.add(value);
        }
    }

    private void switchToBST() {
        if (!(activeTree instanceof BTree)) {
            activeTree = bTree;
            addValuesToActiveTree();
        }
    }

    private void switchToAVL() {
        if (!(activeTree instanceof AVL)) {
            activeTree = avlTree;
            addValuesToActiveTree();
        }
    }

    @FXML
    private void handleScrollZoom(ScrollEvent event) {
        double zoomFactor = 1.1;
        if (event.getDeltaY() < 0) {
            zoomFactor = 1 / zoomFactor;
        }

        scaleFactor *= zoomFactor;
        scaleFactor = Math.max(0.1, scaleFactor);

        pane2.setScaleX(scaleFactor);
        pane2.setScaleY(scaleFactor);
    }

    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        generateRandomTreeValues();
        addValuesToActiveTree();
        drawTree();
        updateBalanceLabel();
    }

    @FXML
    public void containsOnAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Elemento");
        dialog.setHeaderText("Ingrese el número a buscar:");
        dialog.setContentText("Número:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(numberStr -> {
            try {
                int numberToFind = Integer.parseInt(numberStr);
                boolean exists = activeTree.contains(numberToFind);
                String message;
                if (exists) {
                    message = "El número " + numberToFind + " existe en el árbol.";
                } else {
                    message = "El número " + numberToFind + " no existe en el árbol.";
                }
                FXUtility.showAlert("Resultado de la búsqueda", message, Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                FXUtility.showAlert("Error", "Por favor, ingrese un número entero válido.", Alert.AlertType.ERROR);
            } catch (TreeException e) {
                FXUtility.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    public void addOnAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Añadir Elemento");
        dialog.setHeaderText("Ingrese el número a añadir:");
        dialog.setContentText("Número:");

       // Optional<String> result = dialog.showAndWait();

        //result.ifPresent(numberStr -> {
            try {
                int numberToAdd = Utility.random(51);
                activeTree.add(numberToAdd);
                treeValues.add(numberToAdd); // Add to the list of values
                drawTree();
                updateBalanceLabel();
                FXUtility.showAlert("Éxito", "El número " + numberToAdd + " ha sido añadido al árbol.", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                FXUtility.showAlert("Error", "Por favor, ingrese un número entero válido.", Alert.AlertType.ERROR);
            }
        //});
    }

    @FXML
    public void removeOnAction(ActionEvent actionEvent) {
        try {
            Object valueToRemove = getElementToRemove();
            if (valueToRemove == null) return;

            activeTree.remove(valueToRemove);
            treeValues.remove(valueToRemove); // Remove from the list of values
            drawTree();
            updateBalanceLabel();
            FXUtility.showAlert("Nodo Removido", "Se ha eliminado un nodo con valor: " + valueToRemove, Alert.AlertType.INFORMATION);

        } catch (TreeException e) {
            FXUtility.showAlert("Error al Remover", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Object getElementToRemove() throws TreeException {
        if (activeTree.isEmpty()) {
            FXUtility.showAlert("Árbol Vacío", "El árbol está vacío, no hay nodos para eliminar.", Alert.AlertType.WARNING);
            return null;
        }

        List<Object> allElements = new ArrayList<>();
        if (activeTree instanceof BTree) {
            getAllElementsBST((BTree) activeTree, (BTreeNode) ((BTree) activeTree).getRoot(), allElements);
        } else if (activeTree instanceof AVL) {
            getAllElementsAVL((AVL) activeTree, ((AVL) activeTree).getRoot(), allElements);
        }

        if (allElements.isEmpty()) {
            FXUtility.showAlert("Árbol Vacío", "El árbol está vacío, no hay nodos para eliminar.", Alert.AlertType.WARNING);
            return null;
        }

        int randomIndex = Utility.random(allElements.size());
        return allElements.get(randomIndex);
    }

    private void getAllElementsBST(BTree tree, BTreeNode node, List<Object> list) {
        if (node != null) {
            getAllElementsBST(tree, node.left, list);
            list.add(node.data);
            getAllElementsBST(tree, node.right, list);
        }
    }

    private void getAllElementsAVL(AVL tree, BTreeNode node, List<Object> list) {
        if (node != null) {
            getAllElementsAVL(tree, node.left, list);
            list.add(node.data);
            getAllElementsAVL(tree, node.right, list);
        }
    }

    @FXML
    public void nodeHeightOnAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Altura del Nodo");
        dialog.setHeaderText("Ingrese el número del nodo:");
        dialog.setContentText("Número:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(numberStr -> {
            try {
                int nodeValue = Integer.parseInt(numberStr);
                try {
                    int height = activeTree.height(nodeValue);
                    FXUtility.showAlert("Altura del Nodo",
                            "La altura del nodo con valor " + nodeValue + " es: " + height,
                            Alert.AlertType.INFORMATION);
                } catch (TreeException e) {
                    FXUtility.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException e) {
                FXUtility.showAlert("Error", "Por favor, ingrese un número entero válido.", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    public void treeHeightOnAction(ActionEvent actionEvent) {
        try {
            int height = activeTree.height();
            FXUtility.showAlert("Altura del Árbol", "La altura actual del árbol es: " + height, Alert.AlertType.INFORMATION);
        } catch (TreeException e) {
            FXUtility.showAlert("Error", "Ocurrió un error al obtener la altura del árbol: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void drawTree() {
        pane2.getChildren().clear(); // Limpiar el Pane antes de dibujar
        if (!activeTree.isEmpty()) {
            drawBTreeNode(activeTree.getRoot(), pane2.getWidth() / 2, 50, pane2.getWidth() / 4, 0);
        }
        updateBalanceLabel();
    }

    private void drawBTreeNode(BTreeNode node, double x, double y, double hSpace, int level) {
        if (node == null) return;

        double scaledRadius = NODE_RADIUS * scaleFactor;
        Circle circle = new Circle(x, y, scaledRadius, Color.CYAN);
        circle.setStroke(Color.BLACK);

        Text text = new Text(String.valueOf(node.data));
        text.setFont(new Font(12 * scaleFactor));
        text.setX(x - text.getLayoutBounds().getWidth() / 2);
        text.setY(y + text.getLayoutBounds().getHeight() / 4);

        pane2.getChildren().addAll(circle, text);

        double childY = y + LEVEL_SPACING * scaleFactor;
        double hSpaceReductionFactor = 0.7 + (level * 0.07);
        double childHSpace = hSpace * hSpaceReductionFactor * scaleFactor;

        if (node.left != null) {
            double leftX = x - hSpace / 2 * scaleFactor;
            Line line = new Line(x, y + scaledRadius, leftX, childY - scaledRadius);
            pane2.getChildren().add(line);
            drawBTreeNode(node.left, leftX, childY, childHSpace, level + 1);
        }

        if (node.right != null) {
            double rightX = x + hSpace / 2 * scaleFactor;
            Line line = new Line(x, y + scaledRadius, rightX, childY - scaledRadius);
            pane2.getChildren().add(line);
            drawBTreeNode(node.right, rightX, childY, childHSpace, level + 1);
        }
    }

    private void updateBalanceLabel() {
        try {
            boolean isBalanced = activeTree.isBalanced();
            balanceLabel.setText("Balance: " + (isBalanced ? "Balanced" : "Not Balanced"));
        } catch (TreeException e) {
            balanceLabel.setText("Balance: Unknown");
        }
    }
}