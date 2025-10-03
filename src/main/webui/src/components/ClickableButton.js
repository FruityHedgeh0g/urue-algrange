import React from 'react';
import styles from './ClickableButton.css';

export interface ClickableButtonProps {
    label: string;
    onClick?: React.MouseEventHandler<HTMLButtonElement>;
    disabled?: boolean;
    type?: 'button' | 'submit' | 'reset';
    className?: string; // pour surcharger/ajouter des classes au besoin
}

export class ClickableButton extends React.Component<ClickableButtonProps> {
    constructor(props: ClickableButtonProps) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick(e: React.MouseEvent<HTMLButtonElement>) {
        const { onClick, disabled } = this.props;
        if (!disabled && onClick) onClick(e);
    }

    render() {
        const { label, disabled, type = 'button', className } = this.props;

        const classes = [styles.button, className].filter(Boolean).join(' ');

        return (
            <button
                type={type}
                className={classes}
                disabled={disabled}
                onClick={this.handleClick}
            >
                {label}
            </button>
        );
    }
}

export default ClickableButton;