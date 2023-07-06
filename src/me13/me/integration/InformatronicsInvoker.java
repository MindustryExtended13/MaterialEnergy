package me13.me.integration;

import arc.struct.Seq;
import information.integration.IInformationFolder;
import information.integration.InformationComponents;
import mindustry.gen.Icon;

public class InformatronicsInvoker {
    public static void load() {
        //coming soon
        InformationComponents.components.add(
                IInformationFolder.build("Material energy", Icon.box, Seq.with())
        );
    }
}