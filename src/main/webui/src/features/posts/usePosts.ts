import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { changePostStatus, createPost, fetchPostById, fetchPosts, PostInput, updatePost } from "./postsApi";
import { PostStatus } from "./types";
import { queryKeys } from "../queryKeys";
import { useAuth } from "../../auth/AuthContext";

/** Les Posts publiés, pour les pages publiques. */
export function usePosts() {
  return useQuery({ queryKey: queryKeys.posts.all, queryFn: () => fetchPosts(false) });
}

export function usePost(postId: string | undefined) {
  return useQuery({
    queryKey: queryKeys.posts.detail(postId),
    queryFn: () => fetchPostById(postId as string, false),
    enabled: Boolean(postId),
  });
}

/** Tous les Posts, Brouillons compris, pour le Bureau. */
export function useAllPosts() {
  return useQuery({ queryKey: queryKeys.posts.admin, queryFn: () => fetchPosts(true) });
}

/** Écrire, modifier, publier et dépublier : réservé au Bureau. */
export function usePostMutations() {
  const queryClient = useQueryClient();
  const { user } = useAuth();
  const invalidate = () => queryClient.invalidateQueries({ queryKey: queryKeys.posts.all });

  const create = useMutation({
    mutationFn: (input: PostInput) => {
      if (!user) throw new Error("Connectez-vous pour écrire une actualité.");
      return createPost(input, { userId: user.userId, firstName: user.firstName, lastName: user.lastName });
    },
    onSuccess: invalidate,
  });
  const update = useMutation({
    mutationFn: (input: { postId: string } & PostInput) => updatePost(input.postId, { title: input.title, content: input.content }),
    onSuccess: invalidate,
  });
  const changeStatus = useMutation({
    mutationFn: (input: { postId: string; status: PostStatus }) => changePostStatus(input.postId, input.status),
    onSuccess: invalidate,
  });

  return { create, update, changeStatus };
}
