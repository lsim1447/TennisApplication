package tennis.utils.python;

import org.python.core.PyInstance;
import org.python.util.PythonInterpreter;

public class MyPythonInterpreter   {

    private PythonInterpreter interpreter = null;
    private String myClassName;
    private String myMethodName;
    private String myFilename;

    public MyPythonInterpreter(String myClassName, String myMethodName, String myFilename)
    {
        this.myClassName = myClassName;
        this.myMethodName = myMethodName;
        this.myFilename = myFilename;
        this.interpreter = new PythonInterpreter();
    }

    public void execfile( final String fileName )
    {
        this.interpreter.execfile(fileName);
    }

    public PyInstance createClass( final String className, final String opts )
    {
        return (PyInstance) this.interpreter.eval(className + "(" + opts + ")");
    }

    public void executeScript(){

        execfile("./src/main/java/tennis/utils/python/" + this.myFilename);

        PyInstance hello = createClass(this.myClassName, "None");

        System.out.println("The return value of '" + this.myFilename + "' => " + hello.invoke(this.myMethodName));
    }

}
