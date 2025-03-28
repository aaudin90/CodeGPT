package ee.carlrobert.codegpt;

import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import ee.carlrobert.codegpt.util.file.FileUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ReferencedFile(String fileName, String filePath, String fileContent) {

  public static ReferencedFile from(File file) {
    return new ReferencedFile(
        file.getName(),
        file.getPath(),
        FileUtil.readContent(file)
    );
  }

  public static ReferencedFile from(VirtualFile virtualFile) {
    return new ReferencedFile(
        virtualFile.getName(),
        virtualFile.getPath(),
        getVirtualFileContent(virtualFile)
    );
  }

  private static String getVirtualFileContent(VirtualFile virtualFile) {
    var documentManager = FileDocumentManager.getInstance();
    var document = documentManager.getDocument(virtualFile);
    if (document != null && documentManager.isDocumentUnsaved(document)) {
      return document.getText();
    }
    return FileUtil.readContent(virtualFile);
  }

  public String getFileExtension() {
    Pattern pattern = Pattern.compile("[^.]+$");
    Matcher matcher = pattern.matcher(fileName);

    if (matcher.find()) {
      return matcher.group();
    }
    return "";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ReferencedFile that = (ReferencedFile) o;
    return Objects.equals(filePath, that.filePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filePath);
  }
}
