import scala.util.Random

@main
def main(args: String*): Unit =
    // Lists -> IntHelper -> Logic -> RobotSimulator -> BinaryTree    

    // Lists:
    
    println("Lists:")
    println()

    val simpleList = (1 to 10).toList map {_ => Random.nextInt(101)}
    println(simpleList)

    println()

    println(Lists.last(simpleList))
    println(Lists.penultimate(simpleList))
    println(Lists.nth(3, simpleList))
    println(Lists.len(simpleList))
    println(Lists.reverse(simpleList))

    println()

    println(Lists.isPalindrome(List(1, 4, 2, 3, 2, 4, 1)))
    println(Lists.flatten(List(List(1, 7), 2, List(9, List(8)))))
    println(Lists.compress(List(1, 1, 1, 1, 2, 3, 3, 1, 1, 4, 5, 5, 5, 5)))
    println(Lists.pack(List(1, 1, 1, 1, 2, 3, 3, 1, 1, 4, 5, 5, 5, 5)))
    println(Lists.rleEncode(List(1, 1, 1, 1, 2, 3, 3, 1, 1, 4, 5, 5, 5, 5)))
    println(Lists.encodeModified(List(1, 1, 1, 1, 2, 3, 3, 1, 1, 4, 5, 5, 5, 5)))
    println(Lists.rleDecode(List( (3, 'a'), (2, 'z'), (4, 'b') )))
    println(Lists.duplicate(List(1, 2, 3)))
    println(Lists.duplicateN(3, List(1, 2, 3)))
    println(Lists.dropN(3, List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')))
    println(Lists.split(3, List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')))
    println(Lists.slice(3, 6, List('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')))

    println()

    println(Lists.range(1, 101))

    println()

    println(Lists.randomSelect(4, simpleList))

    println(Lists.lotto(7, 101))

    println()

    // IntHelper:

    println("IntHelper:")
    println()

    println(IntHelper.isPrime(7))
    println(IntHelper.gcd(56, 16))
    println(IntHelper.isCoprime(35, 64))
    println(IntHelper.totient(10))
    println(IntHelper.primeFactors(124))
    println(IntHelper.listPrimesInRange(7 to 31))
    println(IntHelper.goldbach(28))

    println()

    // Logic:

    println("Logic:")
    println()

    Logic.truthTable(Logic.not)
    println()

    Logic.truthTable(Logic.and)
    println()

    Logic.truthTable(Logic.or)
    println()

    Logic.truthTable(Logic.nand)
    println()

    Logic.truthTable(Logic.xor)
    println()

    Logic.truthTable(Logic.impl)
    println()

    Logic.truthTable(Logic.equ)
    println()

    // RobotSimulator:

    println("RobotSimulator:")
    println()

    val robotSimulator = RobotSimulator(Random.nextInt(10), Random.nextInt(10))
    println(robotSimulator.position)
    println(robotSimulator.direction)

    println()
    val robotCommands = (1 to 10).toList map {_ => RobotCommand.values(Random.nextInt(RobotCommand.values.length))}
    println(robotCommands)
    println(robotSimulator.walk(robotCommands))

    println()
    val robotCommandsString = "N".repeat(10) map {_ => RobotCommand.values(Random.nextInt(RobotCommand.values.length)) match
        case RobotCommand.Go => 'G'
        case RobotCommand.TurnLeft => 'L'
        case RobotCommand.TurnRight => 'R'
    }
    println(robotCommandsString)
    println(robotSimulator.walk(robotCommandsString))

    println()

    // BinaryTree:

    println("BinaryTree:")
    println()

    println(simpleList)
    var balancedTree = Tree.makeBalanced(simpleList)
    println(balancedTree)
    println(balancedTree.find(5))
    println(balancedTree.remove(5))
    println(balancedTree.min)

    println()
    var tree = Node(Random.nextInt(101)).addValue(Random.nextInt(101)).addValue(Random.nextInt(101)).addValue(Random.nextInt(101)).addValue(Random.nextInt(101))
    println(tree)
    println(tree.find(5))
    println(tree.remove(5))
    println(tree.min)
