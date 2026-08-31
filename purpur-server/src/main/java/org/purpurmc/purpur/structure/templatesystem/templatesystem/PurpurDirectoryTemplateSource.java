package org.purpurmc.purpur.structure.templatesystem.templatesystem;

import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplateSource;
import org.slf4j.Logger;

public class PurpurDirectoryTemplateSource extends TemplateSource {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final DataFixer fixerUpper;
    protected final HolderGetter<Block> blockLookup;
    protected final Path customSource;

    public PurpurDirectoryTemplateSource(DataFixer fixerUpper, HolderGetter<Block> blockLookup, Path customSource) {
        super(fixerUpper, blockLookup);
        this.fixerUpper = fixerUpper;
        this.blockLookup = blockLookup;
        this.customSource = customSource;
    }

    @Override
    public Optional<StructureTemplate> load(Identifier id) {
        String pathString = id.getPath();

        if (pathString.startsWith("minecraft/structure/")) {
            pathString = pathString.substring("minecraft/structure/".length());
        } else if (pathString.startsWith("structure/")) {
            pathString = pathString.substring("structure/".length());
        }

        Path file = customSource.resolve(id.getNamespace()).resolve("structure").resolve(pathString + ".snbt");
        if (!Files.exists(file)) {
            file = customSource.resolve(id.getNamespace()).resolve("structures").resolve(pathString + ".snbt");
        }

        if (!Files.exists(file)) {
            return Optional.empty();
        }

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String input = org.apache.commons.io.IOUtils.toString(reader);
            CompoundTag tag = NbtUtils.snbtToStructure(input);

            StructureTemplate structureTemplate = new StructureTemplate();
            int version = NbtUtils.getDataVersion(tag, 500);
            structureTemplate.load(blockLookup, DataFixTypes.STRUCTURE.updateToCurrentVersion(fixerUpper, tag, version));

            return Optional.of(structureTemplate);
        } catch (Exception e) {
            LOGGER.error("Failed to load test template from {}", file, e);
            return Optional.empty();
        }
    }

    @Override
    public Stream<Identifier> list() {
        List<Identifier> foundTemplates = new ArrayList<>();
        for (String dirName : List.of("structure", "structures")) {
            Path searchFolder = customSource.resolve("minecraft").resolve(dirName);
            if (!Files.isDirectory(searchFolder)) continue;

            try (Stream<Path> walk = Files.walk(searchFolder)) {
                walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".snbt"))
                    .forEach(p -> {
                        String relative = searchFolder.relativize(p).toString().replace("\\", "/");
                        String cleanName = relative.substring(0, relative.length() - ".snbt".length());
                        foundTemplates.add(Identifier.fromNamespaceAndPath("minecraft", "minecraft/structure/" + cleanName));
                    });
            } catch (IOException e) {
                LOGGER.error("Failed to list files in the export folder", e);
            }
        }
        return foundTemplates.stream();
    }
}
