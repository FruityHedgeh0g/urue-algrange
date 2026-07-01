import React from "react";
import Button from "../../atoms/Button/Button";
import styles from "./AdminListItem.module.css";

export interface AdminListItemProps {
  title: string;
  subtitle?: string;
  badge?: React.ReactNode;
  /** Contenu additionnel affiché sous le sous-titre, toujours visible (ex : badges de permissions). */
  footer?: React.ReactNode;
  editing?: boolean;
  onToggleEdit?: () => void;
  editDisabled?: boolean;
  editDisabledReason?: string;
  children?: React.ReactNode;
}

export const AdminListItem: React.FC<AdminListItemProps> = ({
  title,
  subtitle,
  badge,
  footer,
  editing,
  onToggleEdit,
  editDisabled,
  editDisabledReason,
  children,
}) => (
  <li className={styles.item}>
    <div className={styles.row}>
      <div className={styles.info}>
        <div className={styles.titleLine}>
          <span className={styles.title}>{title}</span>
          {badge}
        </div>
        {subtitle && <p className={styles.subtitle}>{subtitle}</p>}
        {footer && <div className={styles.footer}>{footer}</div>}
      </div>
      {onToggleEdit &&
        (editDisabled ? (
          <span className={styles.disabledNote}>{editDisabledReason}</span>
        ) : (
          <Button label={editing ? "Fermer" : "Modifier"} variant="outline" onClick={onToggleEdit} />
        ))}
    </div>
    {editing && !editDisabled && <div className={styles.form}>{children}</div>}
  </li>
);

export default AdminListItem;
