/*                                                                            *\
**           _____  __  _____  __     ____                                    **
**          / ___/ / / /____/ / /    /    \    FieldKit                       **
**         / ___/ /_/ /____/ / /__  /  /  /    (c) 2010, FIELD                **
**        /_/        /____/ /____/ /_____/     http://www.field.io            **
\*                                                                            */
/* created May 15, 2009 */
package field.kit.util

/**
* Helpers to resolve and load Files
* @author Marcus Wendt
*/
object Loader extends Logger {
	import java.net.URL
	import java.io.File

	/**
	* Tries to resolve the given <code>String</code> to an <code>URL</code> in various ways
	*/
	def resolveToURL(path:String):URL = {
		import java.net.MalformedURLException

		if(path == null) return null

		var pathEscaped = path.replaceAll(" ","%20")

		var url:URL = null

		// 1. via URL
		try {
			url = new URL(pathEscaped)
		} catch {
			case e:MalformedURLException => url = null
		}

		// 2. via ClassLoader
		if(url == null) url = ClassLoader.getSystemResource(pathEscaped)

		// 3. via local File path
		if(url == null) {
			val localFile = new File(path)
			if(localFile.exists())
				url = localFile.toURI.toURL
		}

		url
	}


	/**
	* Tries to read the file from the given <code>URL</code> into a <code>String</code>
	*/
	def read(url:URL) = {
		import java.io.BufferedReader
		import java.io.InputStreamReader

		try {
			var buffer = ""
			val reader = new BufferedReader(new InputStreamReader(url.openStream))
			var line = ""
			while (line != null) {
				line = reader.readLine
				if(line != null)
					buffer += line + "\n"
			}
			buffer
			} catch {
				case(e:Exception) =>
					warn(e)
					null
		}
	}

	/**
	* Tries to read the given <code>File</code> into a <code>String</code>
	*/
	def read(file:File) = {
		import java.io.FileReader
		import java.io.BufferedReader

		info("reading", file)

		try {
			var buffer = ""
			val reader = new BufferedReader(new FileReader(file))
			var line = ""
			while (line != null) {
				line = reader.readLine
				if(line != null)
					buffer += line + "\n"
			}
			buffer
		} catch {
			case(e:Exception) =>
			warn(e)
			null
		}
	}
}
