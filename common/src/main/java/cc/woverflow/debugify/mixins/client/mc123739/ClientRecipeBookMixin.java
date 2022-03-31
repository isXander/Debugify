package cc.woverflow.debugify.mixins.client.mc123739;

import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Mixin(ClientRecipeBook.class)
public abstract class ClientRecipeBookMixin {

    @Redirect(method = "reload", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", ordinal = 0))
    private void sortRecipes(Map<RecipeBookGroup, List<List<Recipe<?>>>> map, BiConsumer<RecipeBookGroup, List<List<Recipe<?>>>> action) {
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
