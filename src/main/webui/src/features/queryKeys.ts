/**
 * Clés react-query de toutes les fonctionnalités. Chaque clé de détail est
 * préfixée par la clé de liste : invalider `x.all` rafraîchit aussi les détails.
 */
export const queryKeys = {
  events: { all: ["events"] as const, detail: (eventId: string | undefined) => ["events", "detail", eventId] as const },
  myRegistrations: { all: ["my-event-registrations"] as const },
  sectors: { all: ["sectors"] as const, detail: (sectorId: string | undefined) => ["sectors", "detail", sectorId] as const },
  members: {
    all: ["members"] as const,
    list: ["members", "list"] as const,
    byGroups: (groupIds: string[]) => ["members", "byGroups", ...groupIds] as const,
  },
  posts: { all: ["posts"] as const, detail: (postId: string | undefined) => ["posts", "detail", postId] as const },
  medias: { all: ["medias"] as const },
  carousel: { all: ["carousel-items"] as const, active: ["carousel-items", "active"] as const },
  configurations: { all: ["configurations"] as const },
  featureFlags: { all: ["feature-flags"] as const },
  featureRequests: { all: ["feature-requests"] as const },
};
