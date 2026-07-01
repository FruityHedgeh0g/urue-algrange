import React, { useState } from "react";
import { useCarouselItems, useCarouselMutations } from "../../features/carousel/useCarousel";
import { CarouselItemInput } from "../../features/carousel/types";
import { useMedias } from "../../features/medias/useMedias";
import AdminListItem from "../../components/molecules/AdminListItem/AdminListItem";
import FormField from "../../components/molecules/FormField/FormField";
import Select from "../../components/atoms/Select/Select";
import Checkbox from "../../components/atoms/Checkbox/Checkbox";
import Button from "../../components/atoms/Button/Button";
import Badge from "../../components/atoms/Badge/Badge";
import Spinner from "../../components/atoms/Spinner/Spinner";
import ConfirmDialog from "../../components/molecules/ConfirmDialog/ConfirmDialog";
import { assetUrl } from "../../lib/assetUrl";
import styles from "./CarouselAdminPage.module.css";

const LOGO_OPTION_VALUE = "";
const logoSrc = assetUrl("logo_asso_transparent.png");

interface Draft {
  title: string;
  caption: string;
  mediaId: string;
  linkTo: string;
  active: boolean;
}

const emptyDraft: Draft = { title: "", caption: "", mediaId: LOGO_OPTION_VALUE, linkTo: "", active: true };

function draftToInput(draft: Draft): CarouselItemInput {
  return {
    title: draft.title,
    caption: draft.caption,
    mediaId: draft.mediaId || null,
    linkTo: draft.linkTo.trim() || null,
    active: draft.active,
  };
}

export const CarouselAdminPage: React.FC = () => {
  const { data: items, isLoading } = useCarouselItems();
  const { data: medias, isLoading: mediasLoading } = useMedias();
  const { create, update, remove, move } = useCarouselMutations();
  const [editingId, setEditingId] = useState<string | null>(null);
  const [draft, setDraft] = useState<Draft>(emptyDraft);
  const [creating, setCreating] = useState(false);
  const [newItem, setNewItem] = useState<Draft>(emptyDraft);
  const [toDeleteId, setToDeleteId] = useState<string | null>(null);

  if (isLoading || mediasLoading) return <Spinner label="Chargement du carrousel..." />;

  const mediaOptions = [
    { value: LOGO_OPTION_VALUE, label: "Logo de l'association (par défaut)" },
    ...(medias ?? []).map((m) => ({ value: m.mediaId, label: m.alt })),
  ];

  const imageSrcFor = (mediaId: string) => medias?.find((m) => m.mediaId === mediaId)?.url ?? logoSrc;

  const startEdit = (itemId: string) => {
    const item = items?.find((i) => i.id === itemId);
    if (!item) return;
    setEditingId(itemId);
    setDraft({
      title: item.title,
      caption: item.caption,
      mediaId: item.mediaId ?? LOGO_OPTION_VALUE,
      linkTo: item.linkTo ?? "",
      active: item.active,
    });
  };

  const handleSave = (itemId: string) => (e: React.FormEvent) => {
    e.preventDefault();
    update.mutate({ id: itemId, ...draftToInput(draft) }, { onSuccess: () => setEditingId(null) });
  };

  const handleCreate = (e: React.FormEvent) => {
    e.preventDefault();
    create.mutate(draftToInput(newItem), {
      onSuccess: () => {
        setNewItem(emptyDraft);
        setCreating(false);
      },
    });
  };

  const toDelete = items?.find((i) => i.id === toDeleteId);
  const sortedItems = items ?? [];

  return (
    <div className={styles.wrapper}>
      <div className={styles.toolbar}>
        <h2 className={styles.title}>Carrousel</h2>
        <Button label={creating ? "Annuler" : "+ Nouvel élément"} variant={creating ? "outline" : "primary"} onClick={() => setCreating((v) => !v)} />
      </div>
      <p className={styles.hint}>
        Éléments affichés dans le carrousel de la page d'accueil, dans l'ordre ci-dessous. Un élément inactif reste
        enregistré mais n'apparaît plus sur le site.
      </p>

      {creating && (
        <form className={styles.form} onSubmit={handleCreate} noValidate>
          <FormField label="Titre" value={newItem.title} onChange={(e) => setNewItem({ ...newItem, title: e.target.value })} required />
          <FormField
            label="Légende"
            value={newItem.caption}
            onChange={(e) => setNewItem({ ...newItem, caption: e.target.value })}
          />
          <Select
            label="Image"
            value={newItem.mediaId}
            onChange={(mediaId) => setNewItem({ ...newItem, mediaId })}
            options={mediaOptions}
          />
          <FormField
            label="Lien (optionnel)"
            placeholder="/evenements ou /#benevolat"
            value={newItem.linkTo}
            onChange={(e) => setNewItem({ ...newItem, linkTo: e.target.value })}
          />
          <Checkbox label="Actif" checked={newItem.active} onChange={(active) => setNewItem({ ...newItem, active })} />
          <Button type="submit" label="Créer l'élément" disabled={create.isPending} />
        </form>
      )}

      <ul className={styles.list}>
        {sortedItems.map((item, index) => (
          <AdminListItem
            key={item.id}
            title={item.title}
            subtitle={item.caption}
            leading={<img className={styles.thumb} src={imageSrcFor(item.mediaId ?? LOGO_OPTION_VALUE)} alt="" />}
            badge={<Badge label={item.active ? "Actif" : "Inactif"} tone={item.active ? "accent" : "muted"} />}
            footer={item.linkTo ? <span className={styles.link}>Lien : {item.linkTo}</span> : undefined}
            editing={editingId === item.id}
            onToggleEdit={() => (editingId === item.id ? setEditingId(null) : startEdit(item.id))}
            actions={
              <div className={styles.reorder}>
                <button
                  type="button"
                  className={styles.reorderButton}
                  aria-label="Monter"
                  disabled={index === 0 || move.isPending}
                  onClick={() => move.mutate({ id: item.id, direction: "up" })}
                >
                  ↑
                </button>
                <button
                  type="button"
                  className={styles.reorderButton}
                  aria-label="Descendre"
                  disabled={index === sortedItems.length - 1 || move.isPending}
                  onClick={() => move.mutate({ id: item.id, direction: "down" })}
                >
                  ↓
                </button>
              </div>
            }
          >
            <form className={styles.form} onSubmit={handleSave(item.id)} noValidate>
              <FormField label="Titre" value={draft.title} onChange={(e) => setDraft({ ...draft, title: e.target.value })} required />
              <FormField label="Légende" value={draft.caption} onChange={(e) => setDraft({ ...draft, caption: e.target.value })} />
              <Select label="Image" value={draft.mediaId} onChange={(mediaId) => setDraft({ ...draft, mediaId })} options={mediaOptions} />
              <FormField
                label="Lien (optionnel)"
                placeholder="/evenements ou /#benevolat"
                value={draft.linkTo}
                onChange={(e) => setDraft({ ...draft, linkTo: e.target.value })}
              />
              <Checkbox label="Actif" checked={draft.active} onChange={(active) => setDraft({ ...draft, active })} />
              <div className={styles.formActions}>
                <Button type="submit" label="Enregistrer" disabled={update.isPending} />
                <Button type="button" label="Supprimer" variant="danger" onClick={() => setToDeleteId(item.id)} disabled={update.isPending} />
              </div>
            </form>
          </AdminListItem>
        ))}
      </ul>

      <ConfirmDialog
        isOpen={toDeleteId !== null}
        title="Supprimer cet élément du carrousel ?"
        message={toDelete ? `L'élément « ${toDelete.title} » sera définitivement supprimé du carrousel.` : ""}
        pending={remove.isPending}
        onCancel={() => setToDeleteId(null)}
        onConfirm={() => {
          if (!toDeleteId) return;
          remove.mutate(toDeleteId, {
            onSuccess: () => {
              setToDeleteId(null);
              setEditingId(null);
            },
          });
        }}
      />
    </div>
  );
};

export default CarouselAdminPage;
