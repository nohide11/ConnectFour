package connectfour

fun main() {
    val regex: Regex = Regex("\\s*?[56789]\\s*?X\\s*?[56789]\\s*?")
    println("Connect Four")
    println("First player's name:")
    val firstPlayer = readLine().toString()
    println("Second player's name:")
    val secondPlayer = readLine().toString()
    println("Set the board dimensions (Rows x Columns)")
    println("Press Enter for default (6 x 7)")
    var row = 6
    var col = 7
    do {
        var fake1 = readLine()!!.toUpperCase()
        if (fake1 == "") fake1 = "6 X 7"
        var fake: String = ""
        var x = 0
        var count = 0
        var trigger = 0
        for(i in fake1) {
            if (i in '\u0030'..'\u0039') {
                fake += i
                count++
            } else if (i == 'X') x++
            else if (i != ' ') trigger++
        }
        if (regex.matches(fake1)) {
            row = fake[0].toString().toInt()
            col = fake[1].toString().toInt()
        } else if (x == 0 || count < 2 || trigger != 0){
            println("Invalid input")
            println("Set the board dimensions (Rows x Columns)")
            println("Press Enter for default (6 x 7)")
        } else if (fake[0].toString().toInt() !in 5..9){
            println("Board rows should be from 5 to 9")
            println("Set the board dimensions (Rows x Columns)")
            println("Press Enter for default (6 x 7)")
        } else if (fake[1].toString().toInt() !in 5..9){
            println("Board columns should be from 5 to 9")
            println("Set the board dimensions (Rows x Columns)")
            println("Press Enter for default (6 x 7)")
        }
    } while(!regex.matches(fake1))
    var field = MutableList(row) {
        MutableList(col) { ' ' }
    }

    var multipleGames = ""
    var int: Int = 0
    for (i in 0..int) {
        println("Do you want to play single or multiple games?")
        println("For a single game, input 1 or press Enter")
        println("Input a number of games:")
        multipleGames = readLine().toString()
        if (multipleGames == "") multipleGames = "1"
        else if (multipleGames.toIntOrNull() == null) {
            println("Invalid input")
            int++
        } else if (multipleGames.toInt() <= 0) {
            println("Invalid input")
            int++
        }
    }

    println("$firstPlayer VS $secondPlayer \n$row X $col board")
    println (if (multipleGames.toInt() == 1) "Single game" else "Total $multipleGames games")
    var count = 0
    for (i in 1 .. multipleGames.toInt()) {
        if (multipleGames.toInt() != 1) println("Game #$i")
        count = if (i % 2 == 0) 1 else 0
        gameLogic(field, row, col, firstPlayer, secondPlayer, count)
        END = 0
        println("Score")
        println("$firstPlayer: $scoreF $secondPlayer: $scoreS")
    }
    println("Game over!")
}

var scoreF = 0
var scoreS = 0
var END = 0

fun gameLogic(field:MutableList<MutableList<Char>>, row: Int, col: Int, firstPlayer: String, secondPlayer: String, count1: Int) {
    var count = 0 + count1
    var columnPlayer = ""
    var c: Int = 0
    loop@while (END == 0) {
        if (count % 2 == 0) {
            fieldOutput(row, col, field)
            while (c == 0) {
                println("$firstPlayer's turn:")
                columnPlayer = readLine()!!
                if (columnPlayer == "end") {
                    println("Game over!")
                    END = 1
                    break@loop
                } else if (columnPlayer.toIntOrNull() == null) {
                    println("Incorrect column number")
                } else if (columnPlayer.toInt() !in 1..col) {
                    println("The column number is out of range (1 - $col)")
                } else if (field[0][columnPlayer.toInt() - 1] != ' ') {
                    println("Column $columnPlayer is full")
                } else c++
            }
            for (i in 0 until row) {
                if (i == row - 1) {
                    field[i][columnPlayer.toInt() - 1] = 'o'
                    END = condition(field, firstPlayer, columnPlayer.toInt() - 1, row, col, i, scoreF)
                    break
                }else if (field[i + 1][columnPlayer.toInt() - 1] != ' ') {
                    field[i][columnPlayer.toInt() - 1] = 'o'
                    END = condition(field, firstPlayer, columnPlayer.toInt() - 1, row, col, i, scoreF)
                    break
                }
                else continue
            }
            count++
            c = 0
        }
        else {
            fieldOutput(row, col, field)
            while (c == 0) {
                println("$secondPlayer's turn:")
                columnPlayer = readLine()!!
                if (columnPlayer == "end") {
                    println("Game over!")
                    END = 1
                    break@loop
                } else if (columnPlayer.toIntOrNull() == null) {
                    println("Incorrect column number")
                } else if (columnPlayer.toInt() !in 1..col) {
                    println("The column number is out of range (1 - $col)")
                } else if (field[0][columnPlayer.toInt() - 1] != ' ') {
                    println("Column $columnPlayer is full")
                } else c++
            }
            for (i in 0 until row) {
                if (i == row - 1) {
                    field[i][columnPlayer.toInt() - 1] = '*'
                    END = condition(field, secondPlayer, columnPlayer.toInt() - 1, row, col, i, scoreS)
                    break
                }else if (field[i + 1][columnPlayer.toInt() - 1] != ' ') {
                    field[i][columnPlayer.toInt() - 1] = '*'
                    END = condition(field, secondPlayer, columnPlayer.toInt() - 1, row, col, i, scoreS)
                    break
                }
                else continue
            }
            count++
            c = 0
        }
    }
}

fun DrawCheck(field: MutableList<MutableList<Char>>, row:Int, col: Int): Int {
    var count = 0
    for (i in 0 until row) {
        if (!field[i].contains(' ')) count++
    }
    if (count == row) {
        fieldOutput(row,col,field)
        println("It is a draw")
        fieldClear(field, row, col)
        scoreF += 1
        scoreS += 1
        return 1
    }
    return 0
}

val MULTIPLIERSROW1 = arrayOf(1, 0, 1, -1)
val MULTIPLIERSROW2 = arrayOf(-1, 0, -1, 1)
val MULTIPLIERSCOL1 = arrayOf(0, 1, 1, 1)
val MULTIPLIERSCOL2 = arrayOf(0, -1, -1, -1)

fun condition(field: MutableList<MutableList<Char>>, name: String, column: Int, row: Int, col: Int, rowPlayer: Int, score: Int): Int {
    if (DrawCheck(field, row, col) == 1) return 1
    var count = 0
    var fieldOf = MutableList(row + 2) {
        MutableList(col + 2) { ' ' }
    }
    for (i in 0 until row) {
        for (j in 0 until col) {
            fieldOf[i + 1][j + 1] = field[i][j]
        }
    }

    for (k in 0 until  4) {
        for (i in 1..4) {
            if (fieldOf[rowPlayer + 1 + i * MULTIPLIERSROW1[k]][column + 1 + i * MULTIPLIERSCOL1[k]] == field[rowPlayer][column]) count++ else break
        }
        for (i in 1..4) {
            if (fieldOf[rowPlayer + 1 + i * MULTIPLIERSROW2[k]][column + 1 + i * MULTIPLIERSCOL2[k]] == field[rowPlayer][column]) count++ else break
        }
        if (count == 3) {
            if (field[rowPlayer][column] == 'o') scoreF+=2 else scoreS += 2
            fieldOutput(row, col, field)
            println("Player $name won")
            fieldClear(field, row, col)
            return 1
        } else count = 0
    }
    return 0
}

fun fieldClear(field: MutableList<MutableList<Char>>, row: Int, col: Int) {
    for (i in 0 until row) {
        for (j in 0 until  col) {
            field[i][j] = ' '
        }
    }
}
fun fieldOutput(row: Int, col: Int, field: MutableList<MutableList<Char>>) {
    for (i in 1..col) {
        print(" $i")
    }
    println()
    for (i in 0 until row) {
        for (j in 0 until col) {
            print("║${field[i][j]}")
        }
        print("║")
        println()
    }
    for (i in 0 until col * 2 + 1) {
        if (i == 0) print("╚")
        else if (i == col * 2) print("╝")
        else if (i % 2 == 0) print("╩")
        else print("═")
    }
    println()
}