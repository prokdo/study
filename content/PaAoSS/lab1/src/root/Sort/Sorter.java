package root.Sort;

import java.util.ArrayList;

public final class Sorter {
    private Sorter() {}

    private static final void swap(ArrayList<Number> array, int i, int j) {
        Number temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }


    // Bubble sort:
    public static final long bubbleSort(ArrayList<Number> array) {
        long startTime = System.currentTimeMillis();

        boolean isSorted = false;

        while (!isSorted) {
            isSorted = true;

            for (int i = 0; i < array.size() - 1; i++) {
                if (NumberComparator.compare(array.get(i), array.get(i + 1)) > 0) {
                    swap(array, i, i + 1);
                    isSorted = false;
                }
            }
        }

        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }



    // Insertion sort:
    public static final long insertionSort(ArrayList<Number> array) {
        long startTime = System.currentTimeMillis();
        
        Number temp;

		for (int i = 1; i < array.size(); i++) {
			temp = array.get(i);
			int j = i;
			while (j > 0 && NumberComparator.compare(array.get(j - 1), temp) > 0) {
				array.set(j, array.get(j - 1));
				j--;
			}
			array.set(j, temp);
		}

        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }



    // Selection sort:
    public static final long selectionSort(ArrayList<Number> array) {
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < array.size() - 1; i++)  
        {  
            int minIndex = i;  
            for (int j = i + 1; j < array.size(); j++){  
                if (NumberComparator.compare(array.get(j), array.get(minIndex)) < 0) {  
                    minIndex = j; 
                }  
            }  
            Number min = array.get(minIndex);   
            array.set(minIndex, array.get(i));
            array.set(i, min); 
        }
        
        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }



    // Merge sort:
    private static final void merge(ArrayList<Number> array, int left, int middle, int right) {
        int size1 = middle - left + 1;
        int size2 = right - middle;
 
        Number L[] = new Number[size1];
        Number R[] = new Number[size2];
 
        for (int i = 0; i < size1; ++i)
            L[i] = array.get(left + i);
        for (int j = 0; j < size2; ++j)
            R[j] = array.get(middle + 1 + j);
 
        int i = 0, j = 0;
 
        int k = left;
        while (i < size1 && j < size2) {
            if (NumberComparator.compare(L[i], R[j]) <= 0) {
                array.set(k, L[i]);
                i++;
            }
            else {
                array.set(k, R[j]);
                j++;
            }
            k++;
        }
 
        while (i < size1) {
            array.set(k, L[i]);
            i++;
            k++;
        }
 
        while (j < size2) {
            array.set(k, R[j]);
            j++;
            k++;
        }
    }

    private static final void mergeSortImpl(ArrayList<Number> array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
 
            mergeSortImpl(array, left, middle);
            mergeSortImpl(array, middle + 1, right);
 
            merge(array, left, middle, right);
        }
    }

    public static final long mergeSort(ArrayList<Number> array) {
        long startTime = System.currentTimeMillis();

        mergeSortImpl(array, 0, array.size() - 1);

        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }



    // Quick sort:
    private static final int quickSortPartition(ArrayList<Number> array, int begin, int end) {
        Number pivot = array.get(end);
        int i = (begin - 1);
 
        for (int j = begin; j < end; j++) {
            if (NumberComparator.compare(array.get(j), pivot) < 0) {
                i++;
                swap(array, i, j);
            }
        }
 
        swap(array, i + 1, end);
        return i + 1;
    }

    private static final void quickSortImpl(ArrayList<Number> array, int begin, int end) {
        if (begin < end) {
            int pi = quickSortPartition(array, begin, end);
 
            quickSortImpl(array, begin, pi - 1);
            quickSortImpl(array, pi + 1, end);
        }
    }

    public static final long quickSort(ArrayList<Number> array) {
        long startTime = System.currentTimeMillis();

        quickSortImpl(array, 0, array.size() - 1);

        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }



    // Shell sort:
    public static final long shellSort(ArrayList<Number> array) {
        long startTime = System.currentTimeMillis();

        for (int gap = array.size() / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < array.size(); i++) {
                Number key = array.get(i);
                int j = i;
                while (j >= gap && NumberComparator.compare(array.get(j - gap), key) > 0) {
                   array.set(j, array.get(j - gap));
                    j -= gap;
                }
                array.set(j, key);
            }
        }

        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }



    // Heap sort:
    private static final void heapify(ArrayList<Number> array, int arraySize, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
 
        if (left < arraySize && NumberComparator.compare(array.get(left), array.get(largest)) > 0)
            largest = left;
 
        if (right < arraySize && NumberComparator.compare(array.get(right), array.get(largest)) > 0)
            largest = right;
 
        if (largest != i) {
            swap(array, i, largest);
            heapify(array, arraySize, largest);
        }
    }

    public static final long heapSort(ArrayList<Number> array) {
        long startTime = System.currentTimeMillis();
 
        for (int i = array.size() / 2 - 1; i >= 0; i--)
            heapify(array, array.size(), i);
 
        for (int i = array.size() - 1; i > 0; i--) {
            swap(array, 0, i);
            heapify(array, i, 0);
        }

        long endTime = System.currentTimeMillis();
        return (endTime - startTime);
    }



    // Tree (BST) sort:
    private final class Node {
        public Number value;
        public Node left;
        public Node right;

        public Node(Number value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    private final class BinaryTree {
        Node root;

        public BinaryTree(ArrayList<Number> array) {
            for (int i = 0; i < array.size(); i++) {
                this.add(array.get(i));
            }
        }

        private final Node addRecursive(Node current, Number value) {
            if (current == null) {
                return new Node(value);
            }

            if (NumberComparator.compare(value, current.value) < 0) {
                current.left = addRecursive(current.left, value);
            }
            else if (NumberComparator.compare(value, current.value) > 0) {
                current.right = addRecursive(current.right, value);
            }
            else {
                return current;
            }

            return current;
        }

        public final void add(Number value) {
            this.root = addRecursive(this.root, value);
        }

        public final ArrayList<Number> traverseInOrder(Node node, ArrayList<Number> array) {
            if (node != null) {
                traverseInOrder(node.left, array);
                array.add(node.value);
                traverseInOrder(node.right, array);
            }

            return array;
        }
    }

    public static final long treeSort(ArrayList<Number> array) {
        long startTime = System.currentTimeMillis();

        BinaryTree tree = new Sorter().new BinaryTree(array);

        long endTime = System.currentTimeMillis();

        array.clear();
        array = tree.traverseInOrder(tree.root, array);

        return (endTime - startTime);
    }
}
