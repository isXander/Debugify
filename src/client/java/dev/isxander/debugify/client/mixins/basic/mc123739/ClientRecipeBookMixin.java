package dev.isxander.debugify.client.mixins.basic.mc123739;

import dev.isxander.debugify.fixes.BugFix;
import dev.isxander.debugify.fixes.FixCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.Recipe;

@BugFix(id = "MC-123739", category = FixCategory.BASIC, env = BugFix.Env.CLIENT)
@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {
    @Redirect(method = "setupCollections", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", ordinal = 0))
    private void sortRecipes(Map<RecipeBookCategories, List<List<Recipe<?>>>> map, BiConsumer<RecipeBookCategories, List<List<Recipe<?>>>> action) {
        map.entrySet().stream().map(entry -> Map.entry(entry.getKey(), entry.getValue()
                .stream()
                .sorted((o1, o2) -> {
                    if (o1.isEmpty() || o2.isEmpty()) {
                        return 0;
                    }

                    return o1.get(0).getId().compareTo(o2.get(0).getId());
                })
                .collect(Collectors.toList())
        )).forEach(entry -> action.accept(entry.getKey(), entry.getValue()));
    }
}
