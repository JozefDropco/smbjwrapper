package ch.swaechter.smbjwrapper;

import ch.swaechter.smbjwrapper.core.AbstractSharedItem;
import ch.swaechter.smbjwrapper.streams.SharedInputStream;
import ch.swaechter.smbjwrapper.streams.SharedOutputStream;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.share.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

public final class SharedFile extends AbstractSharedItem<SharedDirectory> {

    public SharedFile(String serverName, String shareName, String pathName, AuthenticationContext authenticationContext) throws IOException {
        super(serverName, shareName, pathName, authenticationContext);
    }

    protected SharedFile(AbstractSharedItem abstractSharedItem, String pathName) {
        super(abstractSharedItem, pathName);
    }

    public void createFile() {
        File file = diskShare.openFile(smbPath.getPath(), EnumSet.of(AccessMask.GENERIC_ALL), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OVERWRITE_IF, null);
        file.close();
    }

    public void deleteFile() {
        diskShare.rm(smbPath.getPath());
    }

    public InputStream getInputStream() {
        File file = diskShare.openFile(smbPath.getPath(), EnumSet.of(AccessMask.GENERIC_ALL), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);
        return new SharedInputStream(file);
    }

    public OutputStream getOutputStream() {
        File file = diskShare.openFile(smbPath.getPath(), EnumSet.of(AccessMask.GENERIC_ALL), null, SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OVERWRITE_IF, null);
        return new SharedOutputStream(file);
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof SharedFile) {
            SharedFile sharedFile = (SharedFile) object;
            return getSmbPath().equals(sharedFile.getSmbPath());
        } else {
            return false;
        }
    }

    @Override
    protected SharedDirectory createSharedNodeItem(String pathName) {
        return new SharedDirectory(this, pathName);
    }
}
