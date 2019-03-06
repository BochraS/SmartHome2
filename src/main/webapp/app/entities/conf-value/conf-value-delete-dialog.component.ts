import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IConfValue } from 'app/shared/model/conf-value.model';
import { ConfValueService } from './conf-value.service';

@Component({
    selector: 'jhi-conf-value-delete-dialog',
    templateUrl: './conf-value-delete-dialog.component.html'
})
export class ConfValueDeleteDialogComponent {
    confValue: IConfValue;

    constructor(
        protected confValueService: ConfValueService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.confValueService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'confValueListModification',
                content: 'Deleted an confValue'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-conf-value-delete-popup',
    template: ''
})
export class ConfValueDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ confValue }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ConfValueDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.confValue = confValue;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
