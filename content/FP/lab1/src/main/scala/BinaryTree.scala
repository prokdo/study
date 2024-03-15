import scala.math.Ordered.orderingToOrdered

/** Описывает двоичное дерево поиска
 *
 * Реализуйте все нижеприведенные методы классов и объектов, чтобы получить
 * рабочее дерево поиска
 */
sealed abstract class Tree[+T]:

    /** Признак симметричности дерева
     *
     * Вычисляет и возвращает признак симметричности дерева. Для реализации данного метода
     * вы можете создавать вспомогательные методы, помогающие в решении.
     *
     * Например:
     * {{{
     *     scala> Node('a', Node('b'), Node('c')).isSymmetric
     *     res0: Boolean = true
     * }}}
     */
    def isSymmetric: Boolean

    /** Добавляет новый узел в двоичное дерево поиска
     *
     * @param x Значение нового узла дерева
     * @tparam U Тип значения, хранимого в новом узле
     * @return Новый корень дерева поиска, полученного после добавления узла
     */
    def addValue[U >: T: Ordering](x: U): Tree[U]

    /** Находит узел, содержащий указанное значение
     *
     * @param x Искомое значение
     * @tparam U Тип искомого значения
     * @return Узел с искомым значением или None, если такого значения в дереве нет
     */
    def find[U >: T: Ordering](x: U): Option[Node[U]]

    /** Удаляет заданный элемент из дерева поиска
     *
     * @param x Удаляемое значение
     * @tparam U Тип удаляемого значения
     * @return Новый корень полученного дерева поиска
     */
    def remove[U >: T: Ordering](x: U): Tree[U]

    def min: T

end Tree


object Tree:
    /** Строит сбалансированное дерево поиска по списку значений узлов
     *
     * Сбалансированное двоичное дерево поиска - это дерево где для каждой его вершины высота её
     * двух поддеревьев различается не более чем на единицу.
     *
     * @param values Значения узлов дерева
     * @tparam T Тип значения в узле
     * @return Сбалансированное дерево поиска по переданным значениям
     */
    def makeBalanced[T: Ordering](values: List[T]): Tree[T] =
        def makeBalancedRecursion(values: List[T]): Tree[T] = 
            if values.isEmpty then End
            else
                val (left, right) = values.splitAt(values.length / 2)
                Node(right.head, makeBalancedRecursion(left), makeBalancedRecursion(right.tail))
        
        makeBalancedRecursion(values.sorted)

end Tree


case class Node[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T]:
    override def toString: String = s"T($value, $left, $right)"

    override def isSymmetric: Boolean =
        def isMirrored(left: Tree[T], right: Tree[T]): Boolean = 
            (left, right) match
                case (End, End) => true
                case (Node(_, l1, r1), Node(_, l2, r2)) => isMirrored(l1, r2) && isMirrored(r1, l2)
                case _ => false

        isMirrored(left, right)
            

    def addValue[U >: T: Ordering](x: U): Tree[U] = 
        if x < value then Node(value, left.addValue(x), right)
        else Node(value, left, right.addValue(x))

    def find[U >: T: Ordering](x: U): Option[Node[U]] =
        if x == value then Some(this)
        else if x < value then left.find(x)
        else right.find(x)

    def remove[U >: T: Ordering](x: U): Tree[U] =
        if x < value then Node(value, left.remove(x), right)
        else if x > value then Node(value, left, right.remove(x))
        else (left, right) match
            case (End, End) => End
            case (End, _) => right
            case (_, End) => left
            case _ =>
                val min = right.min
                Node(min, left, right.remove(min))

    def min: T =
        left match
            case End => value
            case Node(value, _, _) => value

end Node


case object End extends Tree[Nothing]:
    override def toString: String = "_"

    override def isSymmetric: Boolean = true

    def addValue[U: Ordering](x: U): Tree[U] = Node(x)

    def find[U: Ordering](x: U): Option[Node[U]] = None

    def remove[U: Ordering](x: U): Tree[U] = End

    def min: Nothing = throw new NoSuchElementException("Empty tree min")

end End


object Node:
    def apply[T](value: T): Node[T] = Node(value, End, End)

end Node
