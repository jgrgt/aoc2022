package days

class Day7 : Day(7) {

    override fun partOne(): Any {
        val root = Dir("root", null)
        val system = Day7System(root)
        inputList.drop(1) // skip root
            .forEach { l ->
                if (Day7Util.isCommand(l)) {
                    val cmd = Day7Util.parseCommand(l)
                    system.execute(cmd)
                } else {
                    system.pwd.registerDirectoryContents(l)
                }
            }
        // now we have the file system...
        return 0
    }

    override fun partTwo(): Any {
        return "TODO"
    }
}


sealed class Command
data class CdCommand(val target: String) : Command() {

}

object LsCommand : Command()

object Day7Util {
    fun isCommand(s: String): Boolean {
        return s.startsWith("$")
    }

    fun parseCommand(s: String): Command {
        return if (s.startsWith("$ ls")) {
            LsCommand
        } else if (s.startsWith("$ cd")) {
            CdCommand(s.trim().split(" ")[2].trim())
        } else {
            throw IllegalArgumentException("Cannot parse command $s")
        }
    }
}

class Day7System(root: Dir) {
    fun execute(cmd: Command) {
        when (cmd) {
            is CdCommand -> {
                if (cmd.target == "..") {
                    pwd = pwd.parent()
                } else {
                    pwd = pwd.cd(cmd.target)
                }
            }

            LsCommand -> {} // we could be more strict here...
        }
    }

    var pwd = root
}

sealed class Day7Node(open val name: String)
data class Dir(override val name: String, val parent: Dir?, val children: MutableList<Day7Node> = mutableListOf()) :
    Day7Node(name) {
    fun registerDirectoryContents(l: String) {
        if (l.startsWith("dir")) {
            addChildDir(l.trim().split(" ")[2].trim())
        } else {
            val (sizeString, name) = l.trim().split(" ")
            addChildFile(name, sizeString.toInt())
        }
    }

    private fun addChildFile(name: String, size: Int) {
        children.add(File(name, size))
    }

    fun cd(target: String): Dir {
        val child = getChildDir(target)
        if (child != null) {
            return child
        }

        return addChildDir(target)
    }

    private fun addChildDir(n: String): Dir {
        val newChild = Dir(n, this)
        this.children.add(newChild)
        return newChild
    }

    private fun getChildDir(target: String): Dir? {
        return children.find { it.name == target && it is Dir } as Dir?
    }

    fun parent(): Dir {
        return parent!!
    }
}

data class File(override val name: String, val size: Int) : Day7Node(name)
