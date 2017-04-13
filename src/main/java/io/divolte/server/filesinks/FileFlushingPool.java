package io.divolte.server.filesinks;

import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

import io.divolte.server.AvroRecordBuffer;
import io.divolte.server.config.FileSinkConfiguration;
import io.divolte.server.config.ValidatedConfiguration;
import io.divolte.server.processing.ProcessingPool;

@ParametersAreNonnullByDefault
public class FileFlushingPool extends ProcessingPool<FileFlusher, AvroRecordBuffer> {
    public FileFlushingPool(
            final ValidatedConfiguration vc,
            final String sinkName,
            final FileManager.FileManagerFactory managerFactory) {
        this(vc,
             sinkName,
             vc.configuration().global.hdfs.threads,
             vc.configuration().global.hdfs.bufferSize,
             managerFactory);
    }

    public FileFlushingPool(
            final ValidatedConfiguration vc,
            final String sinkName,
            final int numThreads,
            final int maxQueueSize,
            final FileManager.FileManagerFactory factory) {
        super(numThreads,
              maxQueueSize,
              String.format(
                      "%s Flusher [%s]",
                      vc.configuration().getSinkConfiguration(sinkName, FileSinkConfiguration.class).getReadableType(),
                      Objects.requireNonNull(sinkName)),
              () -> new FileFlusher(
                      vc.configuration().getSinkConfiguration(sinkName, FileSinkConfiguration.class).fileStrategy,
                      factory.create())
              );
    }
}