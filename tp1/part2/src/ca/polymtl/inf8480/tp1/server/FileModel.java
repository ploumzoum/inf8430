
public class FileModel
{
    public String _fileName;
    public String _lastChecksum;
    public boolean _locked;

    public FileModel(String fileName)
    {
        _fileName = fileName;
        _lastChecksum = "";
        _locked = false;
    }
}