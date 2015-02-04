package me.Tecno_Wizard.CakeAttack.misc;

import java.util.List;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;

public class MetaUtils {
    //TODO: change to correct plugin name
    /**
     * Gets whether or not the specified Metadata exists
     *
     * @param objectToCheck
     *            object to check for metadata
     * @param key
     *            Key to check for Metadata
     * @return Whether or not the specifed Metadata exists
     */
    public static boolean hasMetadata(Metadatable objectToCheck, String key) {
        List<MetadataValue> values = objectToCheck.getMetadata(key);
        for (MetadataValue val : values) {
            if (val.getOwningPlugin().getDescription().getName().equals("CakeAttack"))
                return true;
        }
        return false;
    }

    /**
     * Gets the specified Metadata as a boolean. MUST CHECK FOR METADATA BEFORE CALLING.
     *
     * @param objectToCheck
     *            object to get data from
     * @param key
     *            Key to get data from
     * @return Value of metadata
     */
    public static boolean getMetadataValueAsBoolean(Metadatable objectToCheck, String key) {
        List<MetadataValue> values = objectToCheck.getMetadata(key);
        for (MetadataValue val : values) {
            if (val.getOwningPlugin().getDescription().getName()
                    .equals("CakeAttack"))
                return val.asBoolean();
        }
        return false;
    }

    /**
     * Gets the specified Metadata as a String. MUST CHECK FOR METADATA BEFORE
     * CALLING.
     *
     * @param objectToCheck
     *            object to get data from
     * @param key
     *            Key to get data from
     * @return Value of metadata
     */
    public static String getMetadataValueAsString(Metadatable objectToCheck, String key) {
        List<MetadataValue> values = objectToCheck.getMetadata(key);
        for (MetadataValue val : values) {
            if (val.getOwningPlugin().getDescription().getName()
                    .equals("CakeAttack"))
                return val.asString();
        }
        return "";
    }
}