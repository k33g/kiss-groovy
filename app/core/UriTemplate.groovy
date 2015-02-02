package core

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by k33g_org on 31/01/15.
 */
class UriTemplate {
  String template
  Pattern regex
  String[] vars
  Integer charCnt

  HashMap matchString(String uri) {
    HashMap values = []

    if(uri) {
      if (!uri.length().equals(0)) {
        Matcher m = this.regex.matcher(uri)

        if (m.matches()) {
          values.put(m.groupCount(), 1.0f)

          for (Integer i = 0; i < m.groupCount(); i++) {
            String name = this.vars[i]
            def value = m.group(i+1)
            def existingValue = values.get(name)

            if (existingValue) {
              if(!existingValue.equals(value)) {
                  return null
              }
            }
            values.put(this.vars[i], value)
          }
        }
      }
    }
    return values
  }

  UriTemplate(String template) {

    Pattern VARIABLE = Pattern.compile("\\{([a-zA-Z]\\w*)\\}")
    String VARIABLE_REGEX = "(.*?)"
    StringBuilder templateRegex = new java.lang.StringBuilder()
    ArrayList names = new ArrayList()
    Integer charCnt = 0
    Integer start = 0
    Integer end = 0
    Matcher matcher = VARIABLE.matcher(template)

    def appendTemplate = { String _template, Integer _start, Integer _end, StringBuilder regex ->

      for (Integer i = _start; i < _end; i++ ) {
        char c = _template.charAt(i)

        if(!"(.?)".indexOf(c.toString()).equals(-1)) {
          regex.append("\\")
        }
        regex.append(c)
      }
      return _end - _start
    }

    while (matcher.find()) {
      end = matcher.start()
      charCnt = charCnt + appendTemplate(template, start, end, templateRegex)
      templateRegex.append(VARIABLE_REGEX)
      def name = matcher.group(1)
      names.add(name)
      start = matcher.end()
    }

    charCnt = charCnt + appendTemplate(template, start, template.length(), templateRegex)

    String[] strArray = java.lang.reflect.Array.newInstance(java.lang.String.class, names.size())

    this.template = template
    this.charCnt = charCnt
    this.regex = Pattern.compile(templateRegex.toString())
    this.vars = strArray

    names.toArray(this.vars)

  }
}
