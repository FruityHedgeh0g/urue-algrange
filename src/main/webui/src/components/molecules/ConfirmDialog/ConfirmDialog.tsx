import React from "react";
import Modal from "../Modal/Modal";
import Button from "../../atoms/Button/Button";
import styles from "./ConfirmDialog.module.css";

export interface ConfirmDialogProps {
  isOpen: boolean;
  title: string;
  message: string;
  confirmLabel?: string;
  cancelLabel?: string;
  pending?: boolean;
  onConfirm: () => void;
  onCancel: () => void;
}

/** Boîte de confirmation pour une action destructrice (ex : suppression). */
export const ConfirmDialog: React.FC<ConfirmDialogProps> = ({
  isOpen,
  title,
  message,
  confirmLabel = "Supprimer",
  cancelLabel = "Annuler",
  pending = false,
  onConfirm,
  onCancel,
}) => (
  <Modal isOpen={isOpen} onClose={onCancel} title={title}>
    <p className={styles.message}>{message}</p>
    <div className={styles.actions}>
      <Button label={cancelLabel} variant="outline" onClick={onCancel} disabled={pending} />
      <Button label={pending ? "Suppression..." : confirmLabel} variant="danger" onClick={onConfirm} disabled={pending} />
    </div>
  </Modal>
);

export default ConfirmDialog;
