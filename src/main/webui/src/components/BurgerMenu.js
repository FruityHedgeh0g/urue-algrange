import React from 'react';
import styles from './BurgerMenu.css';

export interface BurgerItem {
    id: string;
    label: string;
    href?: string;
    onClick?: () => void;
}

export interface BurgerMenuProps {
    items: BurgerItem[];
    buttonLabel?: string;
}

interface BurgerMenuState {
    open: boolean;
}

export class BurgerMenu extends React.Component<BurgerMenuProps, BurgerMenuState> {
    constructor(props: BurgerMenuProps) {
        super(props);
        this.state = { open: false };

        this.toggle = this.toggle.bind(this);
        this.close = this.close.bind(this);
    }

    toggle() {
        this.setState((s) => ({ open: !s.open }));
    }

    close() {
        this.setState({ open: false });
    }

    render() {
        const { items, buttonLabel = 'Menu' } = this.props;
        const { open } = this.state;

        return (
            <div className={styles.container}>
                <button
                    type="button"
                    className={styles.button}
                    aria-label={buttonLabel}
                    aria-expanded={open}
                    onClick={this.toggle}
                >
                    <span className={`${styles.icon} ${open ? styles.open : ''}`} />
                    <span className={styles.label}>{buttonLabel}</span>
                </button>

                {open && (
                    <nav className={styles.menu}>
                        <ul>
                            {items.map((it) => (
                                <li key={it.id}>
                                    {it.href ? (
                                        <a href={it.href} onClick={this.close} className={styles.item}>
                                            {it.label}
                                        </a>
                                    ) : (
                                        <button
                                            type="button"
                                            onClick={() => {
                                                if (it.onClick) it.onClick();
                                                this.close();
                                            }}
                                            className={styles.item}
                                        >
                                            {it.label}
                                        </button>
                                    )}
                                </li>
                            ))}
                        </ul>
                    </nav>
                )}
            </div>
        );
    }
}

export default BurgerMenu;